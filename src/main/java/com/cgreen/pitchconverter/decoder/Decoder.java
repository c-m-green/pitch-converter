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
    // TODO: Javadoc comment
    public boolean decodeMessage(File inputPath, File outputPath, File wordCollectionPath, Params p) {
        WordCollection wc = new WordCollection(wordCollectionPath.getAbsolutePath());
        if (wc.buildWordCollection()) {
            // TODO: Give time
            LOGGER.info("Words loaded successfully");
        } else {            
            LOGGER.fatal("No word collection found at " + wordCollectionPath.getAbsolutePath());
            return false;
        }
        List<MusicSymbol> music = DecoderUtils.getMusic(inputPath);
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
            return DecoderUtils.writeMessagesToFile(matches, outputPath);
        }
    }
}
