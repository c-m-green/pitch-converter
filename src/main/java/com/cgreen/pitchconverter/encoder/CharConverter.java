package com.cgreen.pitchconverter.encoder;

import java.text.Normalizer;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.SymbolFactory;

public class CharConverter {
    
    private static final char[] NON_CHROMATIC_PITCH_CLASSES = { '0', '2', '4', '5', '7', '9', 'e' };
    private static final char[] CHROMATIC_PITCH_CLASSES = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'e' };
    private static final char[] GERMAN_H_PITCH_CLASSES = { '9', 't', '0', '2', '4', '5', '7', 'e' };
    private static final char[] NON_GERMAN_H_PITCH_CLASSES = { '9', 'e', '0', '2', '4', '5', '7' };

    /**
     * Converts a letter into a musical pitch by direct letter association (e.g., A
     * -> A-natural, B -> B-natural, etc.)
     * 
     * Letters that do not directly correspond to a pitch class (i.e., anything past
     * G or H) are converted through modular arithmetic.
     * 
     * @param ch          input character
     * @param octaveStart the register in which to start the sequence
     * @param useGermanH  Option to include H as a viable base letter. In the
     *                    German naming scheme, 'H' represents B-natural ('B' then
     *                    represents B-flat).
     * @return the converted pitch, or a rest if input
     *         character is not a letter.
     */
    static MusicSymbol letterToPitchLiteral(char ch, int octaveStart, boolean useGermanH) {
        if (!Character.isLetter(ch)) { // if non-letter is passed in
            return SymbolFactory.createSymbol('r', 0);
        } else {
            char in = Normalizer.normalize(ch + "", Normalizer.Form.NFD).toUpperCase().charAt(0);
            char[] pitchClasses = useGermanH ? GERMAN_H_PITCH_CLASSES : NON_GERMAN_H_PITCH_CLASSES;
            int charValue = findCharValue(in);
            return obtainPitch(pitchClasses, charValue, octaveStart);
        }
    }

    /**
     * Attempts to convert a character to a Pitch by using its position in the
     * alphabet as the integer representation of a pitch class.
     * 
     * Starts with 'A' at zero (C-natural) and works forward. Any letters whose
     * indices are out of range are converted using modular arithmetic.
     * 
     * @param ch           input character
     * @param octaveStart  the register in which to start the sequence. For
     *                     example: if 4, then input char 'A' would map to C4.
     * @param isChromatic  toggle the use of accidentals when advancing up through
     *                    the alphabet
     * @return The converted pitch object, or a rest if input character
     *         is not a letter nor number.
     */
    static MusicSymbol alphaNumToPitchDegree(char ch, int octaveStart, boolean isChromatic) {
        if (!Character.isLetterOrDigit(ch)) { // if non-alphanumeric character is passed in
            return SymbolFactory.createSymbol('r', 0);
        } else {
            char[] pitchClasses = (isChromatic) ? CHROMATIC_PITCH_CLASSES : NON_CHROMATIC_PITCH_CLASSES;
            int charValue = findCharValue(ch);
            return obtainPitch(pitchClasses, charValue, octaveStart);
        }
    }

    /**
     * Converts a character into an int based on alphabetical position or its parsed
     * value.
     * 
     * Letters will convert to their index in the alphabet. Char representations of
     * integers will convert to their int value.
     * 
     * @param c Input character.
     * @return -1 if invalid char was passed in
     */
    static int findCharValue(char c) {
        char input = Normalizer.normalize(c + "", Normalizer.Form.NFD).toUpperCase().charAt(0);
        int charValue = -1;
        if (Character.isLetter(input)) {
            charValue = (int) input - 65; // Bring down to 0-25 range
        } else if (Character.isDigit(input)) {
            charValue = Integer.parseInt(input + "");
        }
        return charValue;
    }

    /**
     * Get a pitch based on input values.
     * 
     * @param pitchClasses  set of possible pitch classes
     * @param charValue     the index at which the pitch class will be derived
     *                      from the set of available pitch classes. Values beyond
     *                      the length of the set of pitch classes will be reduced
     *                      using modular arithmetic.
     * @param registerStart the lowest octave at which a pitch will be created
     * @return a musical symbol
     */
    static MusicSymbol obtainPitch(char[] pitchClasses, int charValue, int registerStart) {
        if (charValue == -1) {
            return SymbolFactory.createSymbol('r', 0);
        } else {
            int pitchIndex = charValue % pitchClasses.length;
            int register = charValue / pitchClasses.length + registerStart;
            return SymbolFactory.createSymbol(pitchClasses[pitchIndex], register);
        }
    }

}