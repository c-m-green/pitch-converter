package com.cgreen.pitchconverter.decoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
    
    private String filePath;
    private boolean isBuilt;
    
    WordCollection(String filePath) {
        this.filePath = filePath;
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

    /**
     * Read the text file to amass a collection of words.
     * 
     * @return whether the collection was built
     * @throws FileNotFoundException
     */
    boolean buildWordCollection() {
        // TODO: Don't assume each line in the input file contains only a single word.
        if (isBuilt) {
            return false;
        }
        Scanner s;
        long start = System.currentTimeMillis();
        try {
            File file = new File(filePath);
            s = new Scanner(file);
            while (s.hasNextLine()) {
                String word = s.nextLine().toLowerCase();
                if (word.length() > 1 || word.equalsIgnoreCase("a") || word.equalsIgnoreCase("i")) {
                    insert(word);
                }
            }
            s.close();
            LOGGER.debug("Word collection built in {} s.", (System.currentTimeMillis() - start) / 1000.);
            isBuilt = true;
            return true;
        } catch (FileNotFoundException fnfe) {
            return false;
        }
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
}
