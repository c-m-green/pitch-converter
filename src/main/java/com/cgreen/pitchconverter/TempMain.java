package com.cgreen.pitchconverter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.cgreen.pitchconverter.pitch.Pitch;

public class TempMain {
	public static void main(String[] args) {		
		try {
			launch(args);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void launch(String[] args) throws FileNotFoundException {
		WordCollection wc = new WordCollection(args[0]);
		if (wc.buildWordCollection()) {
			System.out.println("Collected " + wc.getWordCount() + " words.");
		} else {
			System.out.println("Uh-oh! Something went wrong importing the words.");
		}
		List<Pitch> music = StringPitchTranslator.byDegree("hello", 3, true);
		/*char[] pcs = new char[]{''};
		List<Pitch> music = new ArrayList<Pitch>();
		for (char pc : pcs) {
			music.add(new Pitch(pc, 3));
		}*/
		try (PrintWriter musicOut = new PrintWriter("music.txt")  ){
		    for (Pitch p : music) {
		    	musicOut.println(p.toString() + " " + CharTranslator.getLabel(p));
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> cf = StringPitchTranslator.decodeByDegree(music, wc, 0);
		List<String> partials = new ArrayList<String>();
		List<String> perfects = new ArrayList<String>();
		for (String s : cf) {
			if (s.contains("?")) {
				partials.add(s);
			} else {
				perfects.add(s);
			}
		}
		perfects.sort((s1, s2) -> (s1.split(" ").length - s2.split(" ").length));
		partials.sort((s1, s2) -> (s2.length() - s1.length()));
		try (PrintWriter partialsOut = new PrintWriter("partials.txt")  ){
		    for (String s : partials) {
				partialsOut.println(s);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try(PrintWriter perfectsOut = new PrintWriter("perfects.txt")  ){
			for (String s : perfects) {
				perfectsOut.println(s);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*System.out.println("**PERFECT MATCHES**");
		int numResults = Math.min(perfects.size(), 50);
		System.out.println("Top results: ");
		for (int i = 0; i < numResults; i++) {
			System.out.println(i + 1 + ") " + perfects.get(i));
		}
		System.out.println("Partial matches: " + partials.size());*/
	}
	
	private static List<String> decodePitches(List<Pitch> ps) {
		List<String> out = new ArrayList<String>();
		for (Pitch p : ps) {
			out.add(CharTranslator.getLabel(p));
		}
		return out;
	}
	

}
