package com.cgreen.pitchconverter.converter;

import java.util.ArrayList;
import java.util.List;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.util.FileReader;
import com.cgreen.pitchconverter.util.Params;

public final class Encoder {
    public static List<MusicSymbol> encodeMessage(Params p) {
        List<MusicSymbol> music = new ArrayList<MusicSymbol>();
        String message = FileReader.getText(p.getInFile());
        // TODO: Unnecessary to exit here?
        if (message.isEmpty() || message.equals("")) {
            System.out.println("An error occurred while reading the input file.");
            System.exit(1);
        }
        switch(p.getMethod()) {
        case LETTER:
            music = StringConverter.byLetter(message, p.getStripLetters(), p.getUseGermanH());
            break;
        case DEGREE:
            int startOctave = p.isChromatic() ? 4 : 3;
            music = StringConverter.byDegree(message, startOctave, p.isChromatic());
            break;
        }
        return music;
    }
}
