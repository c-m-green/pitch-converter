package com.cgreen.pitchconverter;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cgreen.pitchconverter.pitch.Pitch;

public class StringPitchTranslator {
	/**
	 * Converts a string into a sequence of musical pitches by interpreting each
	 * character as a pitch class.
	 * 
	 * Characters that directly map to a pitch (essentially, A to G) will do so.
	 * Letters that are not converted this way continue the pattern through the
	 * alphabet.
	 * 
	 * @param input        - a String to convert
	 * @param stripLetters - Before converting, remove letters that do not exist as
	 *                     pitch class names.
	 * @param useGermanH   - option to include H as a viable base letter. In the
	 *                     German naming scheme, 'H' represents B-natural ('B' then
	 *                     represents B-flat).
	 * @return - a list of Pitch objects
	 */
	public static List<Pitch> byLetter(String input, boolean stripLetters, boolean useGermanH) {
		List<Pitch> out = new ArrayList<Pitch>();
		String s = input;
		if (stripLetters) {
			String pitchLetters = useGermanH ? "ABCDEFGH" : "ABCDEFG";
			s = stripNonPitchLetters(input, pitchLetters);
		}
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			Pitch p = CharTranslator.letterToPitchLiteral(ch, useGermanH);
			out.add(p);
		}
		return out;
	}

	/**
	 * Converts a string into a sequence of musical pitches by interpreting each
	 * character as a scale degree.
	 * 
	 * Letters lower in the alphabet will be lower pitched; the inverse is also
	 * true.
	 * 
	 * @param input       - a String to convert
	 * @param startOctave - the lowest register in which a pitch will be created
	 * @param isChromatic - option to include chromatic notes. If false, all notes
	 *                    will be part of a C Major scale.
	 * @return
	 */
	public static List<Pitch> byDegree(String input, int startOctave, boolean isChromatic) {
		List<Pitch> out = new ArrayList<Pitch>();
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			Pitch p = CharTranslator.alphaNumToPitchDegree(ch, startOctave, isChromatic);
			out.add(p);
		}
		return out;
	}

	/**
	 * Removes characters from a string that are not also pitch class names.
	 * 
	 * @param input
	 * @param pitchLetters - the collection of note names to retain
	 * @return the stripped string
	 */
	private static String stripNonPitchLetters(String input, String pitchLetters) {
		String output = "";
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			ch = Normalizer.normalize(ch + "", Normalizer.Form.NFD).toUpperCase().charAt(0);
			if (pitchLetters.indexOf(ch) != -1) {
				output += ch;
			}
		}
		return output;
	}

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
			charConversions[i] = CharTranslator.getPossibleCharsByDegree(input.get(i));
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

}
