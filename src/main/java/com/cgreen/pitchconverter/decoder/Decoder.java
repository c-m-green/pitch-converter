package com.cgreen.pitchconverter.decoder;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
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
        // TODO: Farm argument checks out to DecoderHelper
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
        List<MusicSymbol> music = DecoderHelper.getMusic(inputPath);
        if (music == null || music.size() == 0) {
            LOGGER.error("No music was loaded.");
            return false;
        }
        long start = System.currentTimeMillis();
        Set<String> matches = PitchDecoder.decode(music, wc, p.getMethod(), p.getUseGermanH(), p.isChromatic());
        if (matches == null || matches.size() == 0) {
            LOGGER.info("No messages were detected! No output produced.");
            return true;
        } else {
            LOGGER.debug("Decoded in {} s.", (System.currentTimeMillis() - start) / 1000.);
            return DecoderHelper.writeMessagesToFile(matches, outputPath);
        }
    }
}
