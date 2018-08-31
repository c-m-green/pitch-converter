package com.cgreen.pitchconverter;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import com.cgreen.pitchconverter.pitch.Pitch;
import com.cgreen.pitchconverter.pitch.PitchCreator;

public class CharTranslator {
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

	/**
	 * Converts a letter into a musical pitch by direct letter association (e.g., A
	 * -> A-natural, B -> B-natural, etc.)
	 * 
	 * Letters that do not directly correspond to a pitch class (i.e., anything past
	 * G or H) are converted through modular arithmetic.
	 * 
	 * @param ch         - input character
	 * @param useGermanH - Option to include H as a viable base letter. In the
	 *                   German naming scheme, 'H' represents B-natural ('B' then
	 *                   represents B-flat).
	 * @return - the converted Pitch object. Returns a blank pitch if input
	 *         character is not a letter.
	 */
	public static Pitch letterToPitchLiteral(char ch, boolean useGermanH) {
		int register = 4;
		if (!Character.isLetter(ch)) { // if non-letter is passed in
			Pitch blank = PitchCreator.createPitch('?');
			return blank;
		} else {
			char in = Normalizer.normalize(ch + "", Normalizer.Form.NFD).toUpperCase().charAt(0);
			char[] pitchClasses = useGermanH ? new char[] { '9', 't', '0', '2', '4', '5', '7', 'e' }
					: new char[] { '9', 'e', '0', '2', '4', '5', '7' };
			int charValue = findCharValue(in);
			Pitch out = obtainPitch(pitchClasses, charValue, register);
			return out;
		}
	}

	/**
	 * Attempts to convert a character to a Pitch by using its position in the
	 * alphabet as the integer representation of a pitch class.
	 * 
	 * Starts with 'A' at zero (C-natural) and works forward. Any letters whose
	 * indices are out of range are converted using modular arithmetic.
	 * 
	 * @param ch          - input character
	 * @param octaveStart - the register in which to start the sequence. For
	 *                    example: if 4, then input char 'A' would map to C4.
	 * @param isChromatic - toggle the use of accidentals when advancing up through
	 *                    the alphabet
	 * @return The converted pitch object. Returns a blank pitch if input character
	 *         is not a letter nor number.
	 */
	public static Pitch alphaNumToPitchDegree(char ch, int octaveStart, boolean isChromatic) {
		if (!Character.isLetterOrDigit(ch)) { // if non-alphanumeric character is passed in
			Pitch blank = PitchCreator.createPitch('?');
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
				pitchClasses = new char[] { '0', '2', '4', '5', '7', '9', 'e' };
			}
			int charValue = findCharValue(in);
			Pitch out = obtainPitch(pitchClasses, charValue, octaveStart);
			return out;
		}
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
	 * Converts a character into an int based on alphabetical position or its parsed
	 * value.
	 * 
	 * Letters will convert to their index in the alphabet. Char representations of
	 * integers will convert to their int value.
	 * 
	 * @param c - Input character.
	 * @return Char value. Defaults to 2 ("C")
	 */
	private static int findCharValue(char c) {
		int charValue = 2; // default to C, because why not?
		if (Character.isLetter(c)) {
			charValue = (int) c - 65; // Bring down to 0-25 range
		} else if (Character.isDigit(c)) {
			charValue = Integer.parseInt(c + "");
		}
		return charValue;
	}

	/**
	 * Create a Pitch.
	 * 
	 * @param pitchClasses  - set of possible pitch classes
	 * @param charValue     - the index at which the pitch class will be derived
	 *                      from the set of available pitch classes. Values beyond
	 *                      the length of the set of pitch classes will be reduced
	 *                      using modular arithmetic.
	 * @param registerStart - the lowest octave at which a pitch will be created
	 * @return Pitch object
	 */
	private static Pitch obtainPitch(char[] pitchClasses, int charValue, int registerStart) {
		int pitchIndex = charValue % pitchClasses.length;
		int register = charValue / pitchClasses.length + registerStart;
		Pitch out = PitchCreator.createPitch(pitchClasses[pitchIndex], register);
		return out;
	}

	/**
	 * Derive all possible characters that could have converted by degree to the
	 * input Pitch.
	 * 
	 * @param p - Input Pitch
	 * @return String of potential characters
	 */
	protected static String getPossibleCharsByDegree(Pitch p) {
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
	 * Represent a musical pitch in text.
	 * 
	 * @param p - Input pitch
	 * @return Pitch name in English
	 */
	public static String getLabel(Pitch p) {
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