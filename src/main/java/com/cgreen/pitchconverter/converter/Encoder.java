package com.cgreen.pitchconverter.converter;

import java.util.ArrayList;
import java.util.List;

import com.cgreen.pitchconverter.datastore.pitch.Pitch;
import com.cgreen.pitchconverter.util.FileReader;
import com.cgreen.pitchconverter.util.Params;

public final class Encoder {
	public static List<Pitch> encodeMessage(Params p) {
		List<Pitch> music = new ArrayList<Pitch>();
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
