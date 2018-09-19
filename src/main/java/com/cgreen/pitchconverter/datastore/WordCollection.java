package com.cgreen.pitchconverter.datastore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Collection of words used for the pitch converter to find valid words. Words
 * are obtained from a text file.
 * 
 * @author c_m_g
 *
 */
public class WordCollection {
	private Map<String, List<String>> words;
	private String filePath;

	public WordCollection(String filePath) {
		words = new HashMap<String, List<String>>();
		this.filePath = filePath;
	}

	/**
	 * Read the text file to amass a collection of words.
	 * 
	 * @return whether the collection was built
	 * @throws FileNotFoundException
	 */
	public boolean buildWordCollection() throws FileNotFoundException {
		if (words.isEmpty()) {
			Scanner s;
			File file = new File(filePath);
			s = new Scanner(file);
			seedMap();
			while (s.hasNextLine()) {
				String word = s.nextLine();
				if (word.length() > 1 || word.equalsIgnoreCase("a") || word.equalsIgnoreCase("i")) {
					words.get(word.charAt(0) + "").add(word);
				}
			}
			s.close();
			return true;
		} else {
			return false;
		}
	}

	// Create a key for each letter and initialize ArrayLists in one pass.
	private void seedMap() {
		for (int unicode = 97; unicode < 123; unicode++) {
			words.put((char) unicode + "", new ArrayList<String>());
		}
	}

	public boolean containsWord(String query) {
		return words.get(query.charAt(0) + "").contains(query);
	}

	public int getWordCount() {
		int count = 0;
		for (int i = 97; i < 123; i++) {
			count += words.get((char) i + "").size();
		}
		return count;
	}
}