package com.cgreen.pitchconverter.encoder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;

class CharConverterTest {

    private static char[] pitchClasses;
    @BeforeAll
    static void setUp() {
        pitchClasses = new char[] { '9', 'e', '0', '2', '4', '5', '7' };
    }
    
    @Test
    void musicSymbolReturnedShouldBeLiteralConversion() {
        MusicSymbol literal = CharConverter.letterToPitchLiteral('C', 3, false);
        assertEquals('0', literal.getPitchClass());
        
        MusicSymbol resultH = CharConverter.letterToPitchLiteral('h', 3, true);
        assertEquals('e', resultH.getPitchClass());
        
        MusicSymbol resultNotH = CharConverter.letterToPitchLiteral('h', 3, false);
        assertEquals('9', resultNotH.getPitchClass());
        
        MusicSymbol beyondHGerman = CharConverter.letterToPitchLiteral('k', 3, true);
        assertEquals('0', beyondHGerman.getPitchClass());
        
        MusicSymbol beyondHNoGerman = CharConverter.letterToPitchLiteral('j', 4, false);
        assertEquals('0', beyondHNoGerman.getPitchClass());
        assertEquals("5", beyondHNoGerman.getRegister());
        
        MusicSymbol normalized = CharConverter.letterToPitchLiteral('é', 3, false);
        assertEquals('4', normalized.getPitchClass());
        assertEquals("3", normalized.getRegister());
        
        // Method should return a Rest here, with an indeterminable pitch.
        MusicSymbol invalid = CharConverter.letterToPitchLiteral('@', 4, true);
        assertEquals('r', invalid.getPitchClass());
        assertEquals("N/A", invalid.getRegister());
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
        assertEquals(3, CharConverter.findCharValue('d'));
        
        // Test invalid chars
        assertEquals(-1, CharConverter.findCharValue('%'));
        assertEquals(-1, CharConverter.findCharValue(' '));
    }
    
    @Test
    void obtainPitchShouldReturnAppropriateSymbol() {
        MusicSymbol pitch = CharConverter.obtainPitch(pitchClasses, 2, 4);
        assertEquals('0', pitch.getPitchClass());
        assertEquals("4", pitch.getRegister());
        
        MusicSymbol rest = CharConverter.obtainPitch(pitchClasses, -1, 4);
        assertEquals('r', rest.getPitchClass());
        assertEquals("N/A", rest.getRegister());

        MusicSymbol higherPitch = CharConverter.obtainPitch(pitchClasses, 16, 4);
        assertEquals('0', higherPitch.getPitchClass());
        assertEquals("6", higherPitch.getRegister());
    }
}
