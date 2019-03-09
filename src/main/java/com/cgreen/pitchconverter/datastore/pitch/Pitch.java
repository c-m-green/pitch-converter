package com.cgreen.pitchconverter.datastore.pitch;

/**
 * The Pitch class represents a musical pitch.
 * 
 * Pitches use the integer representation of pitch classes to track their
 * frequency. Pitches can be created with or without a register. Pitches use a
 * question mark for unknown registers.
 * 
 * @author Christopher Green
 *
 */
public class Pitch extends MusicSymbol {

    private char pitchClass;
    private String register;

    Pitch(char pitchClass) {
        this.pitchClass = pitchClass;
        this.register = "?";
    }

    Pitch(char pitchClass, int register) {
        this.pitchClass = pitchClass;
        this.register = String.valueOf(register);
    }

    public char getPitchClass() {
        return pitchClass;
    }

    /**
     * Get the pitch's pitch class as an integer.
     * 
     * @return -1 if not found
     */
    public int getPitchClassAsInt() {
        int pc;
        try {
            pc = Integer.parseInt(pitchClass + "");
        } catch (NumberFormatException nfe) {
            if (pitchClass == 't') {
                pc = 10;
            } else if (pitchClass == 'e') {
                pc = 11;
            } else {
                pc = -1;
            }
        }
        return pc;
    }

    public String getRegister() {
        return register;
    }
    
    public boolean isNatural() {
        return !(pitchClass == '1' || pitchClass == '3' || pitchClass == '6' || pitchClass == '8' || pitchClass == 't');
    }

    @Override
    public String toString() {
        return pitchClass + ":" + register;
    }
}
