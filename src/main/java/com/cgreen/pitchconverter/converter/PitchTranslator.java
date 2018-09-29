package com.cgreen.pitchconverter.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cgreen.pitchconverter.datastore.WordCollection;
import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.Pitch;

public class PitchTranslator {

	/**
	 * Allows for English representation of pitches.
	 */
	private static final String[] PITCH_CLASS_LABELS = { "C-natural", "C-sharp/D-flat", "D-natural", "D-sharp/E-flat",
			"E-natural", "F-natural", "F-sharp/G-flat", "G-natural", "G-sharp/A-flat", "A-natural", "A-sharp/B-flat",
			"B-natural" };

	// TODO
	/*
	 * public static String[] decodeByLetter(List<Pitch> input, char key) { for
	 * (Pitch pitch : input) { Pitch p = new Pitch(pitch.getPitchClass());
	 * p.transpose(-(p.getIntRepresentation(key))); } return new String[]{}; }
	 */

	/**
	 * Given a sequence of musical symbols, attempts to decode a message that was encoded by
	 * degree.
	 * 
	 * @param input          - List of symbols to be decoded
	 * @param wc             - WordCollection
	 * @param checkChromatic - If true, assume message was encoded using all twelve
	 *                       notes of a chromatic scale
	 * @return a Set of potential messages
	 */
	// TODO Account for transposition
	public static Set<String> decodeByDegree(List<MusicSymbol> music, WordCollection wc, boolean checkChromatic) {
		List<Pitch> in = new ArrayList<Pitch>();
		for (MusicSymbol ms : music) {
			// If music symbol isn't a rest, add it to our list as a pitch
			if (ms.getPitchClass() != '?') {
				in.add((Pitch) ms);
			}
		}
		String[] charConversions = new String[in.size()];
		int[] conversionLengths = new int[in.size()];
		Set<String> results = new HashSet<String>();
		int iterations = 1;
		for (int i = 0; i < in.size(); i++) {
			String possibleChars = getPossibleCharsByDegree(in.get(i), checkChromatic);
			if (possibleChars.isEmpty()) {
				continue;
			}
			charConversions[i] = possibleChars;
			conversionLengths[i] = charConversions[i].length();
			iterations *= conversionLengths[i];
		}
		int[] comboIndices = new int[conversionLengths.length];
		int index = comboIndices.length - 1;
		// System.out.println("There will be " + iterations + " combinations.");
		for (int i = 0; i < iterations; i++) {
			String combo = "";
			for (int j = 0; j < charConversions.length; j++) {
				try {
					combo += charConversions[j].charAt(comboIndices[j]);
				} catch (NullPointerException npe) {
					continue;
				}
			}
			System.out.println("Current String: " + combo + " (" + (i + 1) + "/" + iterations + ")");
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
			if (wc.containsWord(substr)) {
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
	 * @param p           - Input Pitch
	 * @param isChromatic - Check for chromatic notes. If false, check members of
	 *                    the C Major scale
	 * @return String of potential characters
	 */
	private static String getPossibleCharsByDegree(Pitch p, boolean isChromatic) {
		if (p.getPitchClass() == '?') {
			return "";
		}
		int bottomIndex = 97; // 'a'
		int index;
		if (isChromatic) {
			index = bottomIndex + p.getPitchClassAsInt();
		} else {
			if (p.isNatural()) {
				int pc = p.getPitchClassAsInt();
				index = (pc < 5) ? bottomIndex + pc / 2 : bottomIndex + pc / 2 + 1;
			} else {
				return "";
			}
		}
		String glob = "";
		while (index < bottomIndex + 26) {
			glob += (char) index + "";
			int increase = (isChromatic) ? 12 : 7;
			index += increase;
		}
		return glob;
	}

	/**
	 * Represent a musical symbol in text.
	 * 
	 * @param ms - Input symbol
	 * @return Pitch name or rest
	 */
	public static String getLabel(MusicSymbol ms) {
		try {
			String out = PITCH_CLASS_LABELS[ms.getPitchClass()];
			return out;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return ms.toString();
		}
	}
}
