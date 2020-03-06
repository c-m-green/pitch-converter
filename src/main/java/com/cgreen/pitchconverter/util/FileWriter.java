package com.cgreen.pitchconverter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.converter.PitchTranslator;
import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;

public final class FileWriter {
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static boolean writeMusicToFile(List<MusicSymbol> musicOut, File outputFile, String outputFormat) {
        switch(outputFormat) {
        case "text":
            return writeMusicToTxt(musicOut, outputFile);
        case "musicxml":
            // Do the musicxml thing
            return writeMusicToMusicXml(musicOut, outputFile);
        case "midi":
            // Do the MIDI thing
        default:
            LOGGER.warn("Output format \"{}\" is invalid. Defaulting to .txt output.", outputFormat);
            return writeMusicToTxt(musicOut, outputFile);
        }
        
    }
    
    public static boolean writeMusicToTxt(List<MusicSymbol> musicOut, File outputFile) {
        try {
            PrintWriter pw = new PrintWriter(outputFile.getAbsolutePath());
            for (MusicSymbol ms : musicOut) {
                pw.println(ms.toString() + " " + PitchTranslator.getLabel(ms));
            }
            pw.close();
            LOGGER.info("Wrote to " + outputFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            LOGGER.error("File not found.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static boolean writeMusicToMusicXml(List<MusicSymbol> musicOut, File outputFile) {
        return false;
    }
    
    public static boolean writeMessagesToFile(Set<String> strs, File outputFile) {
        try {
            PrintWriter pw = new PrintWriter(outputFile);
            List<String> perfects = cleanDecodeOutput(strs);
            for (String p : perfects) {
                pw.println(p);
            }
            pw.close();
            System.out.println("Wrote to " + outputFile.getAbsolutePath());
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File not found");
            e.printStackTrace();
        }
        return false;
        
    }
    
    private static List<String> cleanDecodeOutput(Set<String> msgSet) {
        List<String> sortedMsgs = new ArrayList<String>();
        for (String s : msgSet) {
            if (!s.contains("?")) {
                sortedMsgs.add(s);
            }
        }
        sortedMsgs.sort((s1, s2) -> (s1.split(" ").length - s2.split(" ").length));
        return sortedMsgs;
    }
}
