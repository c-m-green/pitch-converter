package com.cgreen.pitchconverter.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.cgreen.pitchconverter.datastore.WordCollection;
import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.SymbolFactory;
import com.cgreen.pitchconverter.util.Method;

class PitchDecoderTest {
    
    private static List<MusicSymbol> bunchOfRests;
    private static WordCollection wc;
    
    @BeforeAll
    static void setUp() {
        bunchOfRests = new ArrayList<MusicSymbol>();
        bunchOfRests.add(SymbolFactory.createSymbol('!'));
        bunchOfRests.add(SymbolFactory.createSymbol('^'));
        bunchOfRests.add(SymbolFactory.createSymbol('*'));
        
        wc = new WordCollection("words_alpha.txt");
        wc.buildWordCollection();
    }
    
    @Test
    void decodeShouldStripRests() {
        Set<String> empty = PitchDecoder.decode(bunchOfRests, wc, Method.DEGREE, true, true, false);
        assertEquals(0, empty.size());
    }
}