package com.cgreen.pitchconverter.util;

import java.io.File;

public class Params {
    private static Params instance = null;
    private File inFile, wordCollectionFile;
    private Mode mode;
    private Method method;
    private boolean verbose, useGermanH, useChromatic, stripNonPitchLetters;
    
    private Params() { }
    
    public static Params getInstance() {
        if (instance == null) {
            instance = new Params();
        }
        return instance;
    }
    
    // for encoding
    public void init(File inFile, Mode mode, Method method, boolean verbose, boolean useGermanH, boolean useChromatic, boolean stripNonPitchLetters) {
        this.inFile = inFile;
        this.mode = mode;
        this.method = method;
        this.verbose = verbose;
        this.useGermanH = useGermanH;
        this.useChromatic = useChromatic;
        this.stripNonPitchLetters = stripNonPitchLetters;
    }
    
    // for decoding
    public void init(File inFile, Mode mode, Method method, File wordCollectionFile, boolean verbose, boolean useGermanH, boolean useChromatic, boolean stripNonPitchLetters) {
        this.inFile = inFile;
        this.mode = mode;
        this.method = method;
        this.wordCollectionFile = wordCollectionFile;
        this.verbose = verbose;
        this.useGermanH = useGermanH;
        this.useChromatic = useChromatic;
        this.stripNonPitchLetters = stripNonPitchLetters;
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
    
    public File getWordCollectionFile() {
        return wordCollectionFile;
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
