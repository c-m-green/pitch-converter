package com.cgreen.pitchconverter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.cgreen.pitchconverter.datastore.pitch.Pitch;
import com.cgreen.pitchconverter.datastore.pitch.PitchCreator;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return textLines;
	}
	
	public static List<Pitch> getMusic(File file) {
		List<Pitch> music = new ArrayList<Pitch>();
		try {
			Scanner s = new Scanner(file);
			while(s.hasNextLine()) {
				String line = s.nextLine();
				if (line.contains("?")) {
					continue;
				}
				String pitchText = line.split(" ")[0];
				Pitch p = PitchCreator.createPitch(pitchText.split(":")[0].charAt(0), Integer.valueOf(pitchText.split(":")[1]));
				music.add(p);
			}
			s.close();
		} catch (FileNotFoundException e) {
			
		}
		return music;
	}
}
