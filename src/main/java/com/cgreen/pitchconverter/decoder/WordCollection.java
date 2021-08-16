package com.cgreen.pitchconverter.decoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Collection of words used for the pitch converter to find valid words. Words
 * are obtained from a text file.
 * 
 * @author c_m_g
 *
 */
public class WordCollection {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int ALPHABET_SIZE = 26;
    private static WordNode root;
    
    private File file;
    private boolean isBuilt;
    
    WordCollection(File file) {
        this.file = file;
        root = new WordNode();
        isBuilt = false;
    }
    
    // trie node
    private static class WordNode {
        WordNode[] children = new WordNode[ALPHABET_SIZE];
        
        boolean isEndOfWord;
        
        WordNode() {
            isEndOfWord = false;
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                children[i] = null;
            }
        }
    };

    /**
     * Read the text file to amass a collection of words.
     * 
     * @return whether the collection was built
     */
    boolean buildWordCollection() {
        // TODO: Don't assume each line in the input file contains only a single word.
        if (isBuilt) {
            LOGGER.debug("Tried to build the same word collection twice.");
            return false;
        }
        long start = System.currentTimeMillis();
        // https://examples.javacodegeeks.com/core-java/io/inputstream/read-line-of-chars-from-console-with-inputstream/
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException fnfe) {
            LOGGER.warn("Unable to access user-specified dictionary.");
            br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("words_alpha.txt")));
        } catch (NullPointerException npe) {
            br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("words_alpha.txt"))); 
        }
        try {
            String word;
            while ((word = br.readLine()) != null) {
                word = word.toLowerCase();
                if (vetWord(word)) {
                    insert(word);
                }
            }
            LOGGER.debug("Word collection built in {} s.", (System.currentTimeMillis() - start) / 1000.);
            isBuilt = true;
        } catch (IOException ioe) {
            LOGGER.debug("Error reading word from file.");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                LOGGER.error("Error closing input stream.");
            }
        }
        return isBuilt;
    }
    
    private boolean vetWord(String word) {
        return (word.length() > 1 || word.equalsIgnoreCase("a") || word.equalsIgnoreCase("i"));
    }

    boolean containsWord(String query) {
        int length = query.length();
        int index;
        WordNode pCrawl = root;
        
        for (int level = 0; level < length; level++) {
            index = query.charAt(level) - 'a';
            
            if (pCrawl.children[index] == null) {
                return false;
            }
            
            pCrawl = pCrawl.children[index];
        }
        return (pCrawl != null && pCrawl.isEndOfWord);
    }
    
    private void insert(String word) {
        int length = word.length();
        int index;
        
        WordNode pCrawl = root;
        
        for (int level = 0; level < length; level++) {
            index = word.charAt(level) - 'a';
            try {
                if (pCrawl.children[index] == null) {
                    pCrawl.children[index] = new WordNode();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // There was a char found that was not a letter, so skip it.
                // e.g., "topsy-turvy" becomes "topsyturvy"
                continue;
            }           
            pCrawl = pCrawl.children[index];
        }
        
        pCrawl.isEndOfWord = true;
    }
}
