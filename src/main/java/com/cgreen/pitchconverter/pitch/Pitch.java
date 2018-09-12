package com.cgreen.pitchconverter.pitch;

/**
 * The Pitch class represents a musical pitch.
 * 
 * Pitches use the integer representation of pitch classes to track their
 * frequency. Pitches can be created with or without a register. Pitches use a
 * question mark for invalid pitch frequencies and/or unknown ranges.
 * 
 * @author c_m_g
 *
 */
public class Pitch {

	private char pitchClass;
	private String register;

	protected Pitch(char pitchClass) {
		this.pitchClass = pitchClass;
		this.register = "?";
	}

	protected Pitch(char pitchClass, int register) {
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
	public int getPitchClassAsInteger() {
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

	@Override
	public String toString() {
		return pitchClass + ":" + register;
	}
}
