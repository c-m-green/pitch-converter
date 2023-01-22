package com.cgreen.pitchconverter.encoder;

import static org.junit.jupiter.api.Assertions.*;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.SymbolFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class StringConverterTest {

    @Test
    void byLetterReturnsExpectedResult() {
        final String input = "Hello, world!";
        final int startOctave = 4;
        List<MusicSymbol> music = StringConverter.byLetter(input, startOctave, false, false, false);
        List<MusicSymbol> expected = new ArrayList<MusicSymbol>();
        expected.add(SymbolFactory.createSymbol('9', 5));
        expected.add(SymbolFactory.createSymbol('4', 4));
        expected.add(SymbolFactory.createSymbol('4', 5));
        expected.add(SymbolFactory.createSymbol('4', 5));
        expected.add(SymbolFactory.createSymbol('9', 6));
        expected.add(SymbolFactory.createSymbol('e', 7));
        expected.add(SymbolFactory.createSymbol('9', 6));
        expected.add(SymbolFactory.createSymbol('2', 6));
        expected.add(SymbolFactory.createSymbol('4', 5));
        expected.add(SymbolFactory.createSymbol('2', 4));
        assertEquals(expected, music);
    }

    @Test
    void byLetterStripsLettersFromInput() {
        final String input = "Hello, world!";
        final int startOctave = 4;
        List<MusicSymbol> music = StringConverter.byLetter(input, startOctave, true, false, false);
        List<MusicSymbol> expected = new ArrayList<MusicSymbol>();
        expected.add(SymbolFactory.createSymbol('4', 4));
        expected.add(SymbolFactory.createSymbol('2', 4));
        assertEquals(expected, music);
    }

    @Test
    void byLetterUsesGermanH() {
        final String input = "Hello, world!";
        final int startOctave = 4;
        List<MusicSymbol> music = StringConverter.byLetter(input, startOctave, false, true, false);
        List<MusicSymbol> expected = new ArrayList<MusicSymbol>();
        expected.add(SymbolFactory.createSymbol('e', 4));
        expected.add(SymbolFactory.createSymbol('4', 4));
        expected.add(SymbolFactory.createSymbol('2', 5));
        expected.add(SymbolFactory.createSymbol('2', 5));
        expected.add(SymbolFactory.createSymbol('7', 5));
        expected.add(SymbolFactory.createSymbol('7', 6));
        expected.add(SymbolFactory.createSymbol('7', 5));
        expected.add(SymbolFactory.createSymbol('t', 6));
        expected.add(SymbolFactory.createSymbol('2', 5));
        expected.add(SymbolFactory.createSymbol('2', 4));
        assertEquals(expected, music);
    }

    @Test
    void byLetterWritesRests() {
        final String input = "Hello, world!";
        final int startOctave = 4;
        List<MusicSymbol> music = StringConverter.byLetter(input, startOctave, false, false, true);
        List<MusicSymbol> expected = new ArrayList<MusicSymbol>();
        expected.add(SymbolFactory.createSymbol('9', 5));
        expected.add(SymbolFactory.createSymbol('4', 4));
        expected.add(SymbolFactory.createSymbol('4', 5));
        expected.add(SymbolFactory.createSymbol('4', 5));
        expected.add(SymbolFactory.createSymbol('9', 6));
        expected.add(SymbolFactory.createSymbol('r', 0));
        expected.add(SymbolFactory.createSymbol('r', 0));
        expected.add(SymbolFactory.createSymbol('e', 7));
        expected.add(SymbolFactory.createSymbol('9', 6));
        expected.add(SymbolFactory.createSymbol('2', 6));
        expected.add(SymbolFactory.createSymbol('4', 5));
        expected.add(SymbolFactory.createSymbol('2', 4));
        expected.add(SymbolFactory.createSymbol('r', 0));
        assertEquals(expected, music);
    }

    @Test
    void byLetterKeepsGermanHWhenStrippingLetters() {
        final String input = "Hello, world!";
        final int startOctave = 4;
        List<MusicSymbol> music = StringConverter.byLetter(input, startOctave, true, true, false);
        List<MusicSymbol> expected = new ArrayList<MusicSymbol>();
        expected.add(SymbolFactory.createSymbol('e', 4));
        expected.add(SymbolFactory.createSymbol('4', 4));
        expected.add(SymbolFactory.createSymbol('2', 4));
        assertEquals(expected, music);
    }

    @Test
    void byDegreeReturnsExpectedResult() {
        final String input = "Hello, world!";
        final int startOctave = 4;
        List<MusicSymbol> music = StringConverter.byDegree(input, startOctave, false, false);
        List<MusicSymbol> expected = new ArrayList<MusicSymbol>();
        expected.add(SymbolFactory.createSymbol('0', 5));
        expected.add(SymbolFactory.createSymbol('7', 4));
        expected.add(SymbolFactory.createSymbol('7', 5));
        expected.add(SymbolFactory.createSymbol('7', 5));
        expected.add(SymbolFactory.createSymbol('0', 6));
        expected.add(SymbolFactory.createSymbol('2', 7));
        expected.add(SymbolFactory.createSymbol('0', 6));
        expected.add(SymbolFactory.createSymbol('5', 6));
        expected.add(SymbolFactory.createSymbol('7', 5));
        expected.add(SymbolFactory.createSymbol('5', 4));
        assertEquals(expected, music);
    }

    @Test
    void byDegreeEncodesChromatically() {
        final String input = "Hello, world!";
        final int startOctave = 4;
        List<MusicSymbol> music = StringConverter.byDegree(input, startOctave, true, false);
        List<MusicSymbol> expected = new ArrayList<MusicSymbol>();
        expected.add(SymbolFactory.createSymbol('7', 4));
        expected.add(SymbolFactory.createSymbol('4', 4));
        expected.add(SymbolFactory.createSymbol('e', 4));
        expected.add(SymbolFactory.createSymbol('e', 4));
        expected.add(SymbolFactory.createSymbol('2', 5));
        expected.add(SymbolFactory.createSymbol('t', 5));
        expected.add(SymbolFactory.createSymbol('2', 5));
        expected.add(SymbolFactory.createSymbol('5', 5));
        expected.add(SymbolFactory.createSymbol('e', 4));
        expected.add(SymbolFactory.createSymbol('3', 4));
        assertEquals(expected, music);
    }

    @Test
    void byDegreeInsertsRests() {
        final String input = "Hello, world!";
        final int startOctave = 4;
        List<MusicSymbol> music = StringConverter.byDegree(input, startOctave, false, true);
        List<MusicSymbol> expected = new ArrayList<MusicSymbol>();
        expected.add(SymbolFactory.createSymbol('0', 5));
        expected.add(SymbolFactory.createSymbol('7', 4));
        expected.add(SymbolFactory.createSymbol('7', 5));
        expected.add(SymbolFactory.createSymbol('7', 5));
        expected.add(SymbolFactory.createSymbol('0', 6));
        expected.add(SymbolFactory.createSymbol('r', 0));
        expected.add(SymbolFactory.createSymbol('r', 0));
        expected.add(SymbolFactory.createSymbol('2', 7));
        expected.add(SymbolFactory.createSymbol('0', 6));
        expected.add(SymbolFactory.createSymbol('5', 6));
        expected.add(SymbolFactory.createSymbol('7', 5));
        expected.add(SymbolFactory.createSymbol('5', 4));
        expected.add(SymbolFactory.createSymbol('r', 0));
        assertEquals(expected, music);
    }

    @Test
    void stripNonPitchLettersStripsNonPitchLetters() {
        final String input = "abcdefghijklmnopqrstuvwxyz";
        final String pitchLetters = "JKLMN";
        assertEquals("JKLMN", StringConverter.stripNonPitchLetters(input, pitchLetters));
    }

    @Test
    void newLineCountsAsRest() {
        final String input = "1\n2\n\n3";
        final int startOctave = 4;
        List<MusicSymbol> music = StringConverter.byDegree(input, startOctave, true, true);
        List<MusicSymbol> expected = new ArrayList<MusicSymbol>();
        expected.add(SymbolFactory.createSymbol('1', 4));
        expected.add(SymbolFactory.createSymbol('r', 0));
        expected.add(SymbolFactory.createSymbol('2', 4));
        expected.add(SymbolFactory.createSymbol('r', 0));
        expected.add(SymbolFactory.createSymbol('r', 0));
        expected.add(SymbolFactory.createSymbol('3', 4));
        assertEquals(expected, music);
    }
}
