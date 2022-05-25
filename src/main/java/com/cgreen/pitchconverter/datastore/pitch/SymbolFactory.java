package com.cgreen.pitchconverter.datastore.pitch;

public final class SymbolFactory {
    static final String PITCH_CLASS_INTS = "0123456789te";
    /**
     * Creates a MusicSymbol.
     * 
     * If a valid pitch class is provided, the MusicSymbol returned will be a
     * Pitch. Otherwise, this method returns a Rest.
     *
     * If a rest is desired, an invalid char (e.g., 'r') will do.
     * 
     * @param pitchClass Integer notation of a pitch. Valid pitches are: 0-9,t,e
     * @param register The register of the pitch. For example, the register of
     *                 middle C would have a value of 4.
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
        return (PITCH_CLASS_INTS.indexOf(pc) != -1);
    }
}
