package com.cgreen.pitchconverter.converter;

import java.util.HashSet;
import java.util.Set;

import com.cgreen.pitchconverter.datastore.WordCollection;
import com.cgreen.pitchconverter.util.FileReader;
import com.cgreen.pitchconverter.util.Params;

public final class Decoder {
	public static Set<String> decodeMessage(Params p) {
		switch(p.getMethod()) {
		case LETTER:
			System.out.println("Sorry, decoding by letter isn't done yet. :-( Try something else...");
			return null;
		case DEGREE:
			Set<String> matches = new HashSet<String>();
			matches = PitchTranslator.decodeByDegree(FileReader.getMusic(p.getInFile()), new WordCollection(p.getWordCollectionFile().getAbsolutePath()), p.isChromatic());
			return matches;
		default:
			return null;
		}
	}
}
