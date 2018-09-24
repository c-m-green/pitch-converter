package com.cgreen.pitchconverter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import com.cgreen.pitchconverter.converter.PitchTranslator;
import com.cgreen.pitchconverter.datastore.pitch.Pitch;

public final class FileWriter {
	public static boolean writeMusicToFile(List<Pitch> musicOut, File outputFile) {
		try {
			PrintWriter pw = new PrintWriter("music.txt");
			for (Pitch p : musicOut) {
				pw.println(p.toString() + " " + PitchTranslator.getLabel(p));
			}
			pw.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
			e.printStackTrace();
		}
		return false;
		
	}
	
	public static boolean writeMessagesToFile(Set<String> strs, File outputFile) {
		return false;
	}
}
