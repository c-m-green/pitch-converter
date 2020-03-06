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
        if (message.isEmpty() || message.equals("")) {
            System.out.println("An error occurred while reading the input file.");
            System.exit(1);
        }
        switch(p.getMethod()) {
        case LETTER:
            music = StringConverter.byLetter(message, p.getStripLetters(), p.getUseGermanH());
            break;
        case DEGREE:
            music = StringConverter.byDegree(message, 3, p.isChromatic());
            break;
        }
        return music;
    }
}
