package com.cgreen.pitchconverter.decoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.Pitch;
import com.cgreen.pitchconverter.util.Method;

public class PitchDecoder {

    /**
     * Allows for English representation of pitches.
     */
    private static final String[] PITCH_CLASS_LABELS = { "C-natural", "C-sharp/D-flat", "D-natural", "D-sharp/E-flat",
            "E-natural", "F-natural", "F-sharp/G-flat", "G-natural", "G-sharp/A-flat", "A-natural", "A-sharp/B-flat",
            "B-natural" };
    
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Given a sequence of musical symbols, attempts to decode a message.
     * 
     * @param music          - music to decipher
     * @param wc             - the dictionary of valid words to reference
     * @param m              - the encoding method to investigate
     * @param useGermanH     - assume German H was used at time of encoding
     * @param checkChromatic - assume chromaticism was employed at time of encoding
     * @return a Set of potential messages
     */
    // TODO Account for transposition
    static Set<String> decode(List<MusicSymbol> music, WordCollection wc, Method m, boolean useGermanH, boolean checkChromatic) {
        Set<String> results = new HashSet<String>();

        // First, create list of Pitches, not Rests
        List<Pitch> in = new ArrayList<Pitch>();
        for (MusicSymbol ms : music) {
            // If music symbol isn't a rest, add it to our list as a pitch
            if (ms.getPitchClass() != 'r') {
                in.add((Pitch) ms);
            }
        }
        
        // If there are no pitches to look at, leave now.
        if (in.isEmpty()) {
            return results;
        }
        
        // Create array to store the potential characters for each pitch
        String[] charConversions = new String[in.size()];
        
        // Keep track of the number of potential characters for each pitch
        int[] conversionLengths = new int[in.size()];
        int iterations = 1;
        
        // For each pitch, get the possible chars it could represent
        for (int i = 0; i < in.size(); i++) {
            String possibleChars = "";
            switch(m) {
            case DEGREE:
                possibleChars = StringSolver.getPossibleCharsByDegree(in.get(i), checkChromatic);
                break;
            case LETTER:
                possibleChars = StringSolver.getPossibleCharsByLetter(in.get(i), useGermanH);
                break;
            default:
                // Should be unreachable
                LOGGER.fatal("An error has occurred.");
                System.exit(1);
            }           
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
            StringBuilder combo = new StringBuilder();
            // Build the string to examine for this iteration.
            for (int j = 0; j < charConversions.length; j++) {
                try {
                    combo.append(charConversions[j].charAt(comboIndices[j]));
                } catch (NullPointerException npe) {
                    continue;
                }
            }
            //System.out.println("Current string: " + combo + " (" + (i + 1) + "/" + iterations + ")");
            List<String> l = StringSolver.getPotentialStrings(combo.toString(), wc);
            for (String s : l) {
                results.add(s);
            }
            comboIndices[index] += 1;
            // Advance the array that indicates which chars to use next time.
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
    
    // TODO: Move this somewhere else.
    /**
     * Represent a musical symbol in text.
     * 
     * @param ms - Input symbol
     * @return Pitch name or rest
     */
    public static String getLabel(MusicSymbol ms) {
        char pc = ms.getPitchClass();
        if (pc != '?') {
            try {
                String out = PITCH_CLASS_LABELS[Integer.valueOf(ms.getPitchClass() + "")];
                return out;
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                if (pc == 't') {
                    return PITCH_CLASS_LABELS[10];
                } else if (pc == 'e') {
                    return PITCH_CLASS_LABELS[11];
                }
            }
        }
        return ms.toString();
    }
}
