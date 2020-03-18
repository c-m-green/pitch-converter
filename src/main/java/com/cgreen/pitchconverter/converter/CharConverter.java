package com.cgreen.pitchconverter.converter;

import java.text.Normalizer;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.SymbolFactory;

public class CharConverter {

    /**
     * Converts a letter into a musical pitch by direct letter association (e.g., A
     * -> A-natural, B -> B-natural, etc.)
     * 
     * Letters that do not directly correspond to a pitch class (i.e., anything past
     * G or H) are converted through modular arithmetic.
     * 
     * @param ch          - input character
     * @param octaveStart - the register in which to start the sequence
     * @param useGermanH  - Option to include H as a viable base letter. In the
     *                   German naming scheme, 'H' represents B-natural ('B' then
     *                   represents B-flat).
     * @return - the converted pitch, or a rest if input
     *         character is not a letter.
     */
    protected static MusicSymbol letterToPitchLiteral(char ch, int octaveStart, boolean useGermanH) {
        if (!Character.isLetter(ch)) { // if non-letter is passed in
            return SymbolFactory.createSymbol('z');
        } else {
            char in = Normalizer.normalize(ch + "", Normalizer.Form.NFD).toUpperCase().charAt(0);
            char[] pitchClasses = useGermanH ? new char[] { '9', 't', '0', '2', '4', '5', '7', 'e' }
                    : new char[] { '9', 'e', '0', '2', '4', '5', '7' };
            int charValue = findCharValue(in);
            MusicSymbol out = obtainPitch(pitchClasses, charValue, octaveStart);
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
     * @return The converted pitch object, or a rest if input character
     *         is not a letter nor number.
     */
    protected static MusicSymbol alphaNumToPitchDegree(char ch, int octaveStart, boolean isChromatic) {
        if (!Character.isLetterOrDigit(ch)) { // if non-alphanumeric character is passed in
            return SymbolFactory.createSymbol('z');
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
            MusicSymbol out = obtainPitch(pitchClasses, charValue, octaveStart);
            return out;
        }
    }

    /**
     * Converts a character into an int based on alphabetical position or its parsed
     * value.
     * 
     * Letters will convert to their index in the alphabet. Char representations of
     * integers will convert to their int value.
     * 
     * @param c - Input character.
     * @return Char value as int
     */
    static int findCharValue(char c) {
        // TODO: Log warning if a char outside of 0-25 range is passed to this method.
        int charValue = -1;
        if (Character.isLetter(c)) {
            charValue = (int) c - 65; // Bring down to 0-25 range
        } else if (Character.isDigit(c)) {
            charValue = Integer.parseInt(c + "");
        }
        return charValue;
    }

    /**
     * Get a pitch based on input values.
     * 
     * @param pitchClasses  - set of possible pitch classes
     * @param charValue     - the index at which the pitch class will be derived
     *                      from the set of available pitch classes. Values beyond
     *                      the length of the set of pitch classes will be reduced
     *                      using modular arithmetic.
     * @param registerStart - the lowest octave at which a pitch will be created
     * @return a musical symbol
     */
    static MusicSymbol obtainPitch(char[] pitchClasses, int charValue, int registerStart) {
        if (charValue == -1) {
            return SymbolFactory.createSymbol('z');
        } else {
            int pitchIndex = charValue % pitchClasses.length;
            int register = charValue / pitchClasses.length + registerStart;
            MusicSymbol out = SymbolFactory.createSymbol(pitchClasses[pitchIndex], register);
            return out;
        }
    }

}