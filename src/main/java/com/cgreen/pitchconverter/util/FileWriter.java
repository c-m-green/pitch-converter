package com.cgreen.pitchconverter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.cgreen.pitchconverter.converter.PitchTranslator;
import com.cgreen.pitchconverter.datastore.pitch.Pitch;

public final class FileWriter {
	public static void writeMusicToFile(List<Pitch> musicOut, File outputFile) {
		try {
			PrintWriter pw = new PrintWriter("music.txt");
			for (Pitch p : musicOut) {
				pw.println(p.toString() + " " + PitchTranslator.getLabel(p));
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
			e.printStackTrace();
		}
		
	}
}
