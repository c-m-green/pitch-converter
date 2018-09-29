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
			Scanner s = new Scanner(file);
			while(s.hasNextLine()) {
				textLines += s.nextLine();
			}
			s.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: The input file " + file.getAbsolutePath() + " was not found!");
			System.exit(1);
		}
		return textLines;
	}
	
	public static List<MusicSymbol> getMusic(File file) {
		List<MusicSymbol> music = new ArrayList<MusicSymbol>();
		try {
			Scanner s = new Scanner(file);
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
			System.out.println("Error: The input file " + file.getAbsolutePath() + " was not found!");
			System.exit(1);
		}
		return music;
	}
}
