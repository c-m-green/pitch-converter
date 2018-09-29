package com.cgreen.pitchconverter.converter;

import java.util.ArrayList;
import java.util.List;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.util.FileReader;
import com.cgreen.pitchconverter.util.Params;

public final class Encoder {
	public static List<MusicSymbol> encodeMessage(Params p) {
		List<MusicSymbol> music = new ArrayList<MusicSymbol>();
		switch(p.getMethod()) {
		case LETTER:
			music = StringConverter.byLetter(FileReader.getText(p.getInFile()), p.getStripLetters(), p.getUseGermanH());
			break;
		case DEGREE:
			music = StringConverter.byDegree(FileReader.getText(p.getInFile()), 3, p.isChromatic());
			break;
		}
		return music;
	}
}
