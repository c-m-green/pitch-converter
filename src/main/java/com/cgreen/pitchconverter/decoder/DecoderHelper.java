package com.cgreen.pitchconverter.decoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.SymbolFactory;

public final class DecoderHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    static List<MusicSymbol> readMusicFromFile(File file) throws FileNotFoundException {
        List<MusicSymbol> music = new ArrayList<MusicSymbol>();
        if (!getFileExtension(file).equals("txt")) {
            return music;
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        // TODO: This currently makes MAJOR assumptions about the content of the file; i.e., everything's in the right format.
        // Maybe this should accept a .csv file?
        try {
            String note;
            while((note = br.readLine().toLowerCase()) != null) {
                if (note.contains("rest")) {
                    music.add(SymbolFactory.createSymbol('r', 0));
                } else {
                    String pitchText = note.split(" ")[0];
                    MusicSymbol p = SymbolFactory.createSymbol(pitchText.split(":")[0].charAt(0), Integer.valueOf(pitchText.split(":")[1]));
                    music.add(p);
                }
            }
        } catch (IOException ioe) {
            LOGGER.error("Error reading music from file.");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                LOGGER.error("Error closing input file stream.");
            }
        }
        return music;
    }
    
    static void writeMessagesToFile(List<String> strs, File outputFile) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(outputFile);
        for (String p : strs) {
            pw.println(p);
        }
        pw.close();
        LOGGER.info("Wrote to " + outputFile.getAbsolutePath());       
    }
    
    private static String getFileExtension(File file) {
        String extension = "";
        String fileName = file.getAbsolutePath();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }
    
    static List<String> cleanDecodeOutput(Set<String> msgSet) {
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
