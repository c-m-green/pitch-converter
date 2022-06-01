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
}
