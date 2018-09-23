package com.cgreen.pitchconverter.util;

import java.io.File;

public class Params {
	private static Params instance = null;
	private File inFile;
	private File[] wordCollections;
	private Mode mode;
	private Method method;
	private String outputFormat;
	private boolean verbose, useGermanH, useChromatic, stripNonPitchLetters;
	
	private Params() { }
	
	public static Params getInstance() {
		if (instance == null) {
			instance = new Params();
		}
		return instance;
	}
	
	// for encoding
	public void init(File inFile, Mode mode, Method method, String outputFormat, boolean verbose, boolean useGermanH, boolean useChromatic, boolean stripNonPitchLetters) {
		this.inFile = inFile;
		this.mode = mode;
		this.method = method;
		this.outputFormat = outputFormat;
		this.verbose = verbose;
		this.useGermanH = useGermanH;
		this.useChromatic = useChromatic;
		this.stripNonPitchLetters = stripNonPitchLetters;
	}
	
	// for decoding
	public void init(File inFile, File outFile, File[] wordCollections, Mode mode, String outputFormat, boolean verbose) {
		this.inFile = inFile;
		this.wordCollections = new File[wordCollections.length];
		this.mode = mode;
		this.outputFormat = outputFormat;
		for (int i = 0; i < wordCollections.length; i++) {
			this.wordCollections[i] = wordCollections[i];
		}
		this.verbose = verbose;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public Method getMethod() {
		return method;
	}
	
	public boolean getVerbose() {
		return verbose;
	}
	
	public File getInFile() {
		return inFile;
	}
	
	public File[] getWordLists() {
		return wordCollections.clone();
	}
	
	public String getOutputFormat() {
		return outputFormat;
	}
	
	public boolean getUseGermanH() {
		return useGermanH;
	}
	
	public boolean isChromatic() {
		return useChromatic;
	}
	
	public boolean getStripLetters() {
		return stripNonPitchLetters;
	}
}
