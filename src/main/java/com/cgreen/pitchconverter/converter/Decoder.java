package com.cgreen.pitchconverter.converter;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cgreen.pitchconverter.datastore.WordCollection;
import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.util.FileReader;
import com.cgreen.pitchconverter.util.Params;

public final class Decoder {
    public static Set<String> decodeMessage(Params p) {
        Set<String> matches = new HashSet<String>();
        WordCollection wc = new WordCollection(p.getWordCollectionFile().getAbsolutePath());
        if (!wc.buildWordCollection()) {
            System.out.println("ERROR: No word collection found at " + p.getWordCollectionFile().getAbsolutePath());
            System.exit(1);
        }
        List<MusicSymbol> music = FileReader.getMusic(p.getInFile());
        if (music == null) {
            return null;
        }
        matches = PitchTranslator.decode(music, wc, p.getMethod(), p.getUseGermanH(), p.isChromatic());
        return matches;
    }
}
