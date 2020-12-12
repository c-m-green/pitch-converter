package com.cgreen.pitchconverter.decoder;

import java.util.ArrayList;
import java.util.List;

import com.cgreen.pitchconverter.datastore.pitch.Pitch;

public final class StringSolver {
    /**
     * Searches a collection of words to identify valid words in an input string.
     * 
     * @param potentialLine - String to be read for words
     * @param wc            - WordCollection
     * @return - List of perfect and partial matches
     */
    static List<String> getPotentialStrings(String potentialLine, WordCollection wc) {
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
                        // Add partial match
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
    static String getPossibleCharsByDegree(Pitch p, boolean isChromatic) {
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
        StringBuilder chars = new StringBuilder();
        while (index < bottomIndex + 26) {
            chars.append((char)index);
            int increase = (isChromatic) ? 12 : 7;
            index += increase;
        }
        return chars.toString();
    }
    
    /**
     * Derive all possible characters that could have converted by letter to the
     * input Pitch.
     * 
     * @param p
     * @param useGermanH
     * @return String of potential characters
     */
    static String getPossibleCharsByLetter(Pitch p, boolean useGermanH) {
        if (p.getPitchClass() == '?') {
            return "";
        }
        final String pitchClasses = useGermanH ? "9t02457e" : "9e02457";
        int bottomIndex = 97; // 'a'
        int index = pitchClasses.indexOf(p.getPitchClass()) + bottomIndex;
        StringBuilder chars = new StringBuilder();
        while (index < bottomIndex + 26) {
            chars.append((char)index);
            index += pitchClasses.length();
        }
        return chars.toString();
    }
}
