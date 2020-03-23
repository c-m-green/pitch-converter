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
    
    public Encoder() { }
    // TODO: Javadoc comment
    // TODO: Farm argument checks out to EncoderHelper
    public boolean encodeMessage(File inputFile, File outputFile, String outputFormat, Params p) {
        List<MusicSymbol> music = getMusic(inputFile, p);
        if (music == null || music.size() == 0) {
            LOGGER.debug("No music created.");
            return false;
        }
        return EncoderHelper.writeMusicToFile(music, outputFile, outputFormat);
    }
    
    private List<MusicSymbol> getMusic(File inputFile, Params p) {
        List<MusicSymbol> music = new ArrayList<MusicSymbol>();
        String message = EncoderHelper.getText(inputFile);
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
