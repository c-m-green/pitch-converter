package com.cgreen.pitchconverter.util;

public class Params {
    private Method method;
    private boolean useGermanH, useChromatic, stripNonPitchLetters, includeRests;
    
    /**
     * 
     * @param method
     * @param useGermanH
     * @param useChromatic
     * @param stripNonPitchLetters
     * @param includeRests
     */
    public Params(Method method, boolean useGermanH, boolean useChromatic, boolean stripNonPitchLetters, boolean includeRests) { 
        this.method = method;
        this.useGermanH = useGermanH;
        this.useChromatic = useChromatic;
        this.stripNonPitchLetters = stripNonPitchLetters;
        this.includeRests = includeRests;
    }
    
    public Method getMethod() {
        return method;
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
    
    public boolean getIncludeRests() {
        return includeRests;
    }
}
