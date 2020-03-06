package com.cgreen.pitchconverter.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;

class CharConverterTest {

    @Test
    void musicSymbolReturnedShouldBeLiteralConversion() {
        MusicSymbol literal = CharConverter.letterToPitchLiteral('C', false);
        assertEquals('0', literal.getPitchClass());
        
        MusicSymbol resultH = CharConverter.letterToPitchLiteral('h', true);
        assertEquals('e', resultH.getPitchClass());
        
        MusicSymbol resultNotH = CharConverter.letterToPitchLiteral('h', false);
        assertEquals('9', resultNotH.getPitchClass());
        
        MusicSymbol beyondHGerman = CharConverter.letterToPitchLiteral('k', true);
        assertEquals('0', beyondHGerman.getPitchClass());
        
        MusicSymbol beyondHNoGerman = CharConverter.letterToPitchLiteral('j', false);
        assertEquals('0', beyondHNoGerman.getPitchClass());
        
        MusicSymbol normalized = CharConverter.letterToPitchLiteral('é', false);
        assertEquals('4', normalized.getPitchClass());
        
        // Method should return a Rest here, with an indeterminable pitch.
        MusicSymbol invalid = CharConverter.letterToPitchLiteral('@', true);
        assertEquals('?', invalid.getPitchClass());
    }
    
    @Test
    void musicSymbolReturnedIsNumToPitch() {
        MusicSymbol firstLetter = CharConverter.alphaNumToPitchDegree('A', 0, false);
        assertEquals('0', firstLetter.getPitchClass());
        
        MusicSymbol normalized = CharConverter.alphaNumToPitchDegree('â', 12, true);
        assertEquals('0', normalized.getPitchClass());
        
        MusicSymbol chromatic = CharConverter.alphaNumToPitchDegree('d', 3, true);
        assertEquals('3', chromatic.getPitchClass());
        
        MusicSymbol nonChromatic = CharConverter.alphaNumToPitchDegree('i', 7, false);
        assertEquals('2', nonChromatic.getPitchClass());
        
    }
    
    @Test
    void charValueDeliversAccurateValue() {
        
        assertEquals(7, CharConverter.findCharValue('7'));
        assertEquals(16, CharConverter.findCharValue('Q'));
        
        // Test invalid chars
        assertEquals(-1, CharConverter.findCharValue('%'));
        assertEquals(-1, CharConverter.findCharValue(' '));
    }
    
    @Test
    void obtainPitchShouldReturnAppropriateSymbol() {
        MusicSymbol pitch = CharConverter.obtainPitch(new char[] { '9', 'e', '0', '2', '4', '5', '7' }, 2, 4);
        assertEquals('0', pitch.getPitchClass());
        
        MusicSymbol rest = CharConverter.obtainPitch(new char[] { '9', 'e', '0', '2', '4', '5', '7' }, -1, 4);
        assertEquals('?', rest.getPitchClass());
    }
}
