package com.cgreen.pitchconverter.datastore.pitch;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Objects;

/**
 * The Pitch class represents a musical pitch.
 * 
 * Pitches use the integer representation of pitch classes to track their
 * frequency.
 * 
 * @author Christopher Green
 *
 */
public class Pitch extends MusicSymbol {

    private String register;
    private char pitchClass;

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

    // https://stackoverflow.com/a/27609
    @Override
    public int hashCode() {
        return new HashCodeBuilder(31, 151).
                append(register).
                append(pitchClass).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Pitch other = (Pitch) obj;

        return (this.register.equals(other.register) &&
                this.pitchClass == other.pitchClass);
    }
}
