package com.cgreen.pitchconverter.encoder;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.util.Params;

public final class Encoder {
    private static final Logger LOGGER = LogManager.getLogger();
    private Params p;
    
    public Encoder(Params p) { 
        this.p = p;
    }
    
    public List<MusicSymbol> encodeMessage(File inputFile, String outputFormat) {
        if (outputFormat == null || outputFormat.isEmpty()) {
            // Default to "text"
            outputFormat = "text";
        }
        List<MusicSymbol> music = EncoderHelper.createMusic(inputFile, p);
        if (music == null || music.size() == 0) {
            LOGGER.debug("No music created.");
        }
        return music;
    }
    
    public void encodeMessageToFile(File inputFile, File outputFile, String outputFormat) {
        EncoderHelper.writeMusicToFile(encodeMessage(inputFile, outputFormat), outputFile, outputFormat);
    }
    
    public void setParams(Params p) {
        this.p = p;
    }
}
