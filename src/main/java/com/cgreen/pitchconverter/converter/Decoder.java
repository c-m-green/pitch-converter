package com.cgreen.pitchconverter.converter;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.datastore.WordCollection;
import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.util.FileReader;
import com.cgreen.pitchconverter.util.Params;

public final class Decoder {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Set<String> decodeMessage(Params p) {
        WordCollection wc = new WordCollection(p.getWordCollectionFile().getAbsolutePath());
        if (wc.buildWordCollection()) {
            // TODO: Give time
            LOGGER.info("Words loaded successfully");
        } else {            
            LOGGER.fatal("No word collection found at " + p.getWordCollectionFile().getAbsolutePath());
            System.exit(1);
        }
        List<MusicSymbol> music = FileReader.getMusic(p.getInFile());
        if (music == null) {
            return null;
        }
        Set<String> matches = PitchTranslator.decode(music, wc, p.getMethod(), p.getUseGermanH(), p.isChromatic(), p.getVerbose());
        return matches;
    }
}
