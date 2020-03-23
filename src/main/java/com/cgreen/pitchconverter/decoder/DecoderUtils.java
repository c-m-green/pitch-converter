package com.cgreen.pitchconverter.decoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.SymbolFactory;

public final class DecoderUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    static List<MusicSymbol> getMusic(File file) {
        List<MusicSymbol> music = new ArrayList<MusicSymbol>();
        try {
            if (!getFileExtension(file).equals("txt")) {
                return null;
            }
            Scanner s = new Scanner(file);
            // TODO: This currently makes MAJOR assumptions about the content of the file; i.e., everything's in the right format.
            // Maybe this should accept a .csv file?
            while(s.hasNextLine()) {
                String line = s.nextLine();
                if (line.contains("rest")) {
                    music.add(SymbolFactory.createSymbol('r', 0));
                } else {
                    String pitchText = line.split(" ")[0];
                    MusicSymbol p = SymbolFactory.createSymbol(pitchText.split(":")[0].charAt(0), Integer.valueOf(pitchText.split(":")[1]));
                    music.add(p);
                }
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: The input file " + file.getAbsolutePath() + " was not found!");
            System.exit(1);
        }
        return music;
    }
    
    static boolean writeMessagesToFile(Set<String> strs, File outputFile) {
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
    
    private static String getFileExtension(File file) {
        String extension = "";
        String fileName = file.getAbsolutePath();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
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
