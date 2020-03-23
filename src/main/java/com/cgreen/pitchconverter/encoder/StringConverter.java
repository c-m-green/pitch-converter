package com.cgreen.pitchconverter.encoder;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;

public class StringConverter {
    /**
     * Converts a string into a sequence of musical pitches by interpreting each
     * character as a pitch class.
     * 
     * Characters that directly map to a pitch (essentially, A to G) will do so.
     * Letters that are not converted this way continue the pattern through the
     * alphabet.
     * 
     * @param input        - a String to convert
     * @param startOctave  - the lowest octave in which a pitch will be created
     * @param stripLetters - Before converting, remove letters that do not exist as
     *                     pitch class names.
     * @param useGermanH   - option to include H as a viable base letter. In the
     *                     German naming scheme, 'H' represents B-natural ('B' then
     *                     represents B-flat).
     * @param writeRests   - option to include rests in output
     * @return - a list of Pitch objects
     */
    static List<MusicSymbol> byLetter(String input, int startOctave, boolean stripLetters, boolean useGermanH, boolean writeRests) {
        List<MusicSymbol> out = new ArrayList<MusicSymbol>();
        String s = input;
        if (stripLetters) {
            final String pitchLetters = useGermanH ? "ABCDEFGH" : "ABCDEFG";
            s = stripNonPitchLetters(input, pitchLetters);
        }
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            MusicSymbol ms = CharConverter.letterToPitchLiteral(ch, startOctave, useGermanH);
            if (ms.getPitchClass() != '?' || writeRests) {
                out.add(ms);
            }
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
     * @param writeRests  - option to include rests in output
     * @return
     */
    static List<MusicSymbol> byDegree(String input, int startOctave, boolean isChromatic, boolean writeRests) {
        List<MusicSymbol> out = new ArrayList<MusicSymbol>();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            MusicSymbol ms = CharConverter.alphaNumToPitchDegree(ch, startOctave, isChromatic);
            if (ms.getPitchClass() != '?' || writeRests) {
                out.add(ms);
            }
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
            if (pitchLetters.indexOf(ch) != -1 || ch == ' ') {
                output += ch;
            }
        }
        return output;
    }

}
