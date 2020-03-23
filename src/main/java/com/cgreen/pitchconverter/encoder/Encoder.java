package com.cgreen.pitchconverter.encoder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.util.Params;

public final class Encoder {
    private static final Logger LOGGER = LogManager.getLogger();
    // TODO: Javadoc comment
    public static boolean encodeMessage(Params p, File outputPath, String outputFormat) {
        List<MusicSymbol> music = getMusic(p);
        if (music == null || music.size() == 0) {
            LOGGER.debug("No music created.");
            return false;
        }
        return EncoderUtils.writeMusicToFile(music, outputPath, outputFormat);
    }
    
    private static List<MusicSymbol> getMusic(Params p) {
        List<MusicSymbol> music = new ArrayList<MusicSymbol>();
        String message = EncoderUtils.getText(p.getInFile());
        // TODO: Unnecessary to exit here?
        if (message.isEmpty() || message.equals("")) {
            LOGGER.debug("The input file was empty.");
            return music;
        }
        int startOctave;
        switch(p.getMethod()) {
        case LETTER:
            startOctave = p.getStripLetters() ? 4 : 3;
            music = StringConverter.byLetter(message, startOctave, p.getStripLetters(), p.getUseGermanH(), p.getIncludeRests());
            break;
        case DEGREE:
            startOctave = p.isChromatic() ? 4 : 3;
            music = StringConverter.byDegree(message, startOctave, p.isChromatic(), p.getIncludeRests());
            break;
        default:
            LOGGER.error("An error has occurred.");
            System.exit(1);
        }
        return music;
    }
}
