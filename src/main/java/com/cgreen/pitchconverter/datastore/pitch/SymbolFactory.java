package com.cgreen.pitchconverter.datastore.pitch;

public final class SymbolFactory {
    static final String PITCH_CLASS_INTS = "0123456789te";
    /**
     * Create a MusicSymbol.
     * 
     * If a valid pitch class (in integer notation) is provided, the
     * MusicSymbol returned will be a Pitch. Otherwise, this method returns
     * a Rest.
     * 
     * Valid pitches are: 0-9,t,e
     * 
     * If a rest is desired, an invalid char (e.g., 'r') will do.
     * 
     * @param pitchClass
     * @param register
     * @return a Pitch of the given pitch class, or a Rest.
     */
    public static MusicSymbol createSymbol(char pitchClass, int register) {
        if (isValidPitchClass(pitchClass)) {
            return new Pitch(pitchClass, register);
        } else {
            return new Rest();
        }
    }

    
    private static boolean isValidPitchClass(char pc) {
        return !(PITCH_CLASS_INTS.indexOf(pc) == -1);
    }
}
