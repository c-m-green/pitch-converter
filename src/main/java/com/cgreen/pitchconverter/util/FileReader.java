package com.cgreen.pitchconverter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.cgreen.pitchconverter.datastore.pitch.Pitch;

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
		return music;
	}
}
