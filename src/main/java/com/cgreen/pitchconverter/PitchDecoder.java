package com.cgreen.pitchconverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cgreen.pitchconverter.pitch.Pitch;

public class PitchDecoder {
	
	/**
	 * Allows for English representation of pitches.
	 */
	private static final String[] PITCH_CLASS_LABELS = { "C-natural", "C-sharp/D-flat", "D-natural", "D-sharp/E-flat",
			"E-natural", "F-natural", "F-sharp/G-flat", "G-natural", "G-sharp/A-flat", "A-natural", "A-sharp/B-flat",
			"B-natural" };
	/**
	 * Used for English representation of indeterminate pitches.
	 */
	private static final String BLANK = "???";

	// TODO
	/*
	 * public static String[] decodeByLetter(List<Pitch> input, char key) { for
	 * (Pitch pitch : input) { Pitch p = new Pitch(pitch.getPitchClass());
	 * p.transpose(-(p.getIntRepresentation(key))); } return new String[]{}; }
	 */

	/**
	 * Given a sequence of pitches, attempts to decode a message that was encoded by
	 * degree.
	 * 
	 * @param input - List of Pitches to be decoded
	 * @param wc    - WordCollection
	 * @return a Set of potential messages
	 */
	// TODO Account for transposition
	public static Set<String> decodeByDegree(List<Pitch> input, WordCollection wc) {
		String[] charConversions = new String[input.size()];
		int[] conversionLengths = new int[input.size()];
		Set<String> results = new HashSet<String>();
		int iterations = 1;
		for (int i = 0; i < input.size(); i++) {
			charConversions[i] = getPossibleCharsByDegree(input.get(i));
			conversionLengths[i] = charConversions[i].length();
			iterations *= conversionLengths[i];
		}
		int[] comboIndices = new int[conversionLengths.length];
		int index = comboIndices.length - 1;
		// System.out.println("There will be " + iterations + " combinations.");
		for (int j = 0; j < iterations; j++) {
			String combo = "";
			for (int i = 0; i < charConversions.length; i++) {
				combo += charConversions[i].charAt(comboIndices[i]);
			}
			System.out.println("Current String: " + combo + " (" + (j + 1) + "/" + iterations + ")");
			List<String> l = getPotentialStrings(combo, wc);
			for (String s : l) {
				results.add(s);
			}
			comboIndices[index] += 1;
			while (comboIndices[index] >= conversionLengths[index]) {
				comboIndices[index] = 0;
				index--;
				if (index < 0) {
					break;
				}
				comboIndices[index] += 1;
			}
			index = comboIndices.length - 1;
		}
		return results;
	}

	/**
	 * Searches a collection of words to identify valid words in an input string.
	 * 
	 * @param potentialLine - String to be read for words
	 * @param wc            - WordCollection
	 * @return - List of perfect and partial matches
	 */
	private static List<String> getPotentialStrings(String potentialLine, WordCollection wc) {
		// System.out.println("Searching for words in: \"" + potentialLine + "\"");
		List<String> possibilities = new ArrayList<String>();
		int head = 0;
		int current = head + 1;
		int foot = potentialLine.length();
		while (current <= foot) {
			String substr = potentialLine.substring(head, current);
			if (wc.isValidWord(substr)) {
				// System.out.println("Found word: " + substr);
				String newQuery = potentialLine.substring(substr.length(), foot);
				if (newQuery.length() > 0) {
					// System.out.println("Now trying: " + newQuery);
					List<String> subResults = getPotentialStrings(newQuery, wc);
					if (subResults.isEmpty()) {
						possibilities.add(substr + "?");
					} else {
						for (String s : subResults) {
							// System.out.println("Adding to the results: " + substr + " " + s);
							possibilities.add(substr + " " + s);
						}
					}
				} else {
					possibilities.add(substr);
				}
			} else {
				// System.out.println("\"" + substr + "\" is not a word.");
			}
			current++;
		}
		// System.out.println("For " + potentialLine + ", returning " +
		// possibilities.size() + " results.");
		return possibilities;
	}
	
	/**
	 * Derive all possible characters that could have converted by degree to the
	 * input Pitch.
	 * 
	 * @param p - Input Pitch
	 * @return String of potential characters
	 */
	private static String getPossibleCharsByDegree(Pitch p) {
		char pitchClass = p.getPitchClass();
		int bottomIndex = 97;
		int index = bottomIndex + getIntRepresentation(pitchClass);
		String glob = "";
		while (index < bottomIndex + 26) {
			glob += (char) index + "";
			index += 12;
		}
		return glob;
	}
	
	/**
	 * Returns the int representation of a pitch class.
	 * 
	 * @param pc - A pitch class as a character.
	 * @return The pitch class as an integer.
	 */
	private static int getIntRepresentation(char pc) {
		int i;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("t", 10);
		map.put("e", 11);
		try {
			i = Integer.parseInt(pc + "");
		} catch (NumberFormatException nfe) {
			// System.out.println(arg + " not parsed -- searching map...");
			try {
				i = map.get(pc + "");
				// System.out.println("Got it!");
			} catch (NullPointerException npe) {
				// System.out.println("Value not found in map: " + arg);
				return -1;
			}
		}
		return i;
	}
	

	/**
	 * Represent a musical pitch in text.
	 * 
	 * @param p - Input pitch
	 * @return Pitch name in English
	 */
	protected static String getLabel(Pitch p) {
		int pitchIndex = getIntRepresentation(p.getPitchClass());
		try {
			String out = PITCH_CLASS_LABELS[pitchIndex];
			return out;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			// System.out.println("Got symbol: " + p.getPitchClass());
			return BLANK;
		}
	}
}
