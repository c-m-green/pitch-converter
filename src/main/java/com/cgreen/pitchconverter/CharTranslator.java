package com.cgreen.pitchconverter;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharTranslator {
	protected static Pitch letterToPitchLiteral(char ch, boolean useGermanH) {
		int register = 4;
		if (!Character.isLetter(ch)) { // if non-letter is passed in
			Pitch blank = new Pitch('?');
			return blank;
		} else {
			char in = Normalizer.normalize(ch + "", Normalizer.Form.NFD).toUpperCase().charAt(0);
			char[] pitchClasses = useGermanH ? new char[]{'9', 't', '0', '2', '4', '5', '7', 'e'} : new char[]{'9', 'e', '0', '2', '4', '5', '7'};
			int charValue = findPitchIndexFromUnicode(in);
			Pitch out = getPitch(pitchClasses, charValue, register);
			return out;
		}
	}
	protected static Pitch alphaNumToPitchDegree(char ch, int octaveStart, boolean isChromatic) {
		if (!Character.isLetterOrDigit(ch)) { // if non-alphanumeric character is passed in
			Pitch blank = new Pitch('?');
			return blank;	
		} else {
			char in = Normalizer.normalize(ch + "", Normalizer.Form.NFD).toUpperCase().charAt(0);
			char[] pitchClasses;
			if (isChromatic) {
				pitchClasses = new char[12];
				for (int i = 0; i < 10; i++) {
					pitchClasses[i] = Character.forDigit(i, 10);
				}
				pitchClasses[10] = 't';
				pitchClasses[11] = 'e';
			} else {
				pitchClasses = new char[]{'0', '2', '4', '5', '7', '9', 'e'};
			}
			int charValue = findPitchIndexFromUnicode(in);
			Pitch out = getPitch(pitchClasses, charValue, octaveStart);
			return out;
		}
	}
	
	protected static int getIntRepresentation(char arg) {
		int i;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("t", 10);
		map.put("e", 11);
		try {
			i = Integer.parseInt(arg + "");
		} catch (NumberFormatException nfe) {
			// System.out.println(arg + " not parsed -- searching map...");
			try {
				i = map.get(arg + "");
				// System.out.println("Got it!");
			} catch (NullPointerException npe) {
				// System.out.println("Value not found in map: " + arg);
				return -1;
			}
		}
		return i;
	}
	
	private static int findPitchIndexFromUnicode(char c) {
		int charValue = 2; // default to C, because why not?
		if (Character.isLetter(c)) {
			charValue = (int) c - 65; // Bring down to 0-25 range
		} else if (Character.isDigit(c)) {
			charValue = Integer.parseInt(c + "");
		}
		return charValue;
	}
	
	private static Pitch getPitch(char[] pitchClasses, int charValue, int registerStart) {
		int pitchIndex = charValue % pitchClasses.length;
		int register = charValue / pitchClasses.length + registerStart;
		Pitch out = new Pitch(pitchClasses[pitchIndex], register);
		return out;
	}
	
	protected static String getPossibleChars(Pitch p) {
		char pitchClass = p.getPitchClass();
		int bottomIndex = 97;
		int index = bottomIndex + getIntRepresentation(pitchClass);
		String glob = "";
		while (index < bottomIndex + 26) {
			glob += (char)index + "";
			index += 12;
		}
		//System.out.print(glob);
		return glob;
	}

}