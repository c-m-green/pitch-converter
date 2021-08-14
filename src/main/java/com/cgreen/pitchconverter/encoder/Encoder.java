package com.cgreen.pitchconverter.encoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.util.OutputFormat;
import com.cgreen.pitchconverter.util.Params;

public final class Encoder {
    private static final Logger LOGGER = LogManager.getLogger();
    private Params p;
    
    public Encoder(Params p) { 
        this.p = p;
    }
    
    public List<MusicSymbol> encodeMessage(File inputFile, OutputFormat outputFormat) throws FileNotFoundException {
        if (!inputFile.isFile()) {
            throw new FileNotFoundException("The input file was not found.");
        }
        List<MusicSymbol> music = EncoderHelper.createMusic(inputFile, p);
        if (music == null || music.isEmpty()) {
            LOGGER.debug("No music created.");
        }
        return music;
    }
    
    public void encodeMessageToFile(File inputFile, File outputFile, OutputFormat outputFormat) throws FileNotFoundException {
        EncoderHelper.writeMusicToFile(encodeMessage(inputFile, outputFormat), outputFile, outputFormat);
    }
    
    public void setParams(Params p) {
        this.p = p;
    }
}
