package com.cgreen.pitchconverter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.SymbolFactory;

public final class FileReader {
    public static String getText(File file) {
        String textLines = "";
        try {
            if (!getFileExtension(file).equals("txt")) {
                return "";
            }
            Scanner s = new Scanner(file);
            while(s.hasNextLine()) {
                textLines += s.nextLine();
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: The input file " + file.getAbsolutePath() + " was not found!");
            System.exit(1);
        }
        return textLines;
    }
    
    public static List<MusicSymbol> getMusic(File file) {
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
                    music.add(SymbolFactory.createSymbol('z'));
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
    
    private static String getFileExtension(File file) {
        String extension = "";
        String fileName = file.getAbsolutePath();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }
}
