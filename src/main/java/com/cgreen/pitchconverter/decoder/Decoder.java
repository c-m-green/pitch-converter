package com.cgreen.pitchconverter.decoder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.Rest;
import com.cgreen.pitchconverter.util.Params;

public final class Decoder {
    private static final Logger LOGGER = LogManager.getLogger();
    
    public Decoder() { }
    
    /**
     * Attempts to decode a message that was encoded into music. Writes to file.
     * 
     * @param inputPath          - path to input .txt file
     * @param outputPath         - path to output .txt file
     * @param wordCollectionPath - optionally overwrite default word dictionary with path to .txt file (`null` to bypass)
     * @param p                  - Params object
     * @return                   - true, if successful
     */
    public boolean decodeMessage(File inputPath, File outputPath, File wordCollectionPath, Params p) {
        if (wordCollectionPath == null) {
            LOGGER.debug("Using internal dictionary.");
        } else {
            LOGGER.debug("Using dictionary supplied by user.");
        }
        WordCollection wc = new WordCollection(wordCollectionPath);
        if (wc.buildWordCollection()) {
            LOGGER.info("Words loaded successfully.");
        } else {            
            LOGGER.fatal("Dictionary of valid words unsuccessfully built.");
            return false;
        }
        List<MusicSymbol> music = DecoderHelper.readMusicFromFile(inputPath);
        if (music == null || music.isEmpty()) {
            LOGGER.error("No music was loaded.");
            return false;
        }
        List<Integer> restIndices = new ArrayList<Integer>();
        for (int i = 0; i < music.size(); i++) {
            if (music.get(i) instanceof Rest) {
                restIndices.add(i);
            }
        }
        Set<String> matches;
        long start = System.currentTimeMillis();
        if (restIndices.isEmpty()) {
            matches = PitchDecoder.decode(music, wc, p.getMethod(), p.getUseGermanH(), p.isChromatic());
        } else {
            matches = new HashSet<String>();
            int startIndex = 0;
            List<List<String>> masterWordList = new ArrayList<List<String>>();
            for (int restIndex : restIndices) {
                if (startIndex < restIndex) {
                    List<MusicSymbol> candidateWord = music.subList(startIndex, restIndex);
                    Set<String> resultWords = PitchDecoder.decode(candidateWord, wc, p.getMethod(), p.getUseGermanH(), p.isChromatic());
                    List<String> possibleWords = new ArrayList<String>();
                    for (String word : resultWords) {
                        if (!word.contains(" ")) {
                            possibleWords.add(word);
                        }
                    }
                    masterWordList.add(possibleWords);
                }
                startIndex = restIndex + 1;
            }
            if (!masterWordList.isEmpty()) {
                int[] wordIndexArr = new int[masterWordList.size()];
                int numCombinations = 1;
                for (int i = 0; i < masterWordList.size(); i++) {
                    numCombinations *= masterWordList.get(i).size();
                }
                int comboIndex = masterWordList.size() - 1;
                for (int i = 0; i < numCombinations; i++) {
                    StringBuilder messageBuilder = new StringBuilder();
                    for (int j = 0; j < masterWordList.size(); j++) {
                        messageBuilder.append(masterWordList.get(j).get(wordIndexArr[j]));
                        if (j < masterWordList.size() - 1) {
                            messageBuilder.append(" ");
                        }
                    }
                    //System.out.println("Possible message " + (i + 1) + " of " + numCombinations + ": " + messageBuilder.toString());
                    matches.add(messageBuilder.toString());
                    // Advance the array that indicates which words to use next time.
                    wordIndexArr[comboIndex] += 1;
                    while (wordIndexArr[comboIndex] >= masterWordList.get(comboIndex).size()) {
                        wordIndexArr[comboIndex] = 0;
                        comboIndex--;
                        if (comboIndex < 0) {
                            break;
                        }
                        wordIndexArr[comboIndex] += 1;
                    }
                    comboIndex = wordIndexArr.length - 1;
                }
            }
        }
        List<String> msgsOut = DecoderHelper.cleanDecodeOutput(matches);
        if (matches == null || matches.isEmpty()) {
            LOGGER.info("No messages were detected! No output produced.");
            return true;
        } else {
            LOGGER.debug("Decoded in {} s.", (System.currentTimeMillis() - start) / 1000.);
            return DecoderHelper.writeMessagesToFile(msgsOut, outputPath);
        }
    }
}
