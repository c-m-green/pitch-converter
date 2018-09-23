package com.cgreen.pitchconverter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
}
