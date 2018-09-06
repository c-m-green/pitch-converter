package com.cgreen.pitchconverter.pitch;

/**
 * The Pitch class represents a musical pitch.
 * 
 * Pitches use the integer representation of pitch classes to track their
 * frequency. Pitches can be created with or without a register. Pitches without
 * known registers return a question mark ('?').
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

	public String getRegister() {
		return register;
	}

	@Override
	public String toString() {
		return pitchClass + ":" + register;
	}
}
