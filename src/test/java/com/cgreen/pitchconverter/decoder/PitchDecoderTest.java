package com.cgreen.pitchconverter.decoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.datastore.pitch.SymbolFactory;
import com.cgreen.pitchconverter.util.Method;

class PitchDecoderTest {
    
    private static List<MusicSymbol> bunchOfRests;
    private static WordCollection wc;
    
    @BeforeAll
    static void setUp() {
        bunchOfRests = new ArrayList<MusicSymbol>();
        bunchOfRests.add(SymbolFactory.createSymbol('!', 0));
        bunchOfRests.add(SymbolFactory.createSymbol('^', 0));
        bunchOfRests.add(SymbolFactory.createSymbol('*', 0));
        
        wc = new WordCollection(null);
        wc.buildWordCollection();
    }
    
    @Test
    void decodeShouldStripRests() {
        Set<String> empty = PitchDecoder.decode(bunchOfRests, wc, Method.DEGREE, true, true);
        assertEquals(0, empty.size());
    }
}
