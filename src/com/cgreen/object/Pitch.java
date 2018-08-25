package com.cgreen.object;

public class Pitch {
	private static final String PITCH_CLASSES = "0123456789te";
	private static final String REGEX = ":";
	private char pitchClass;
	private String register;
	
	public Pitch(char pitchClass) {
		this.pitchClass = '?';
		if (PITCH_CLASSES.indexOf(pitchClass) != -1) {
			this.pitchClass = pitchClass;
		}
		register = "?";
	}
	
	public Pitch(char pitchClass, int register) {
		this(pitchClass);
		this.register = String.valueOf(register);
	}
	
	public char getPitchClass() {
		return pitchClass;
	}
	
	public String getRegister() {
		return register;
	}
	
	public boolean transpose(int interval) {
		if (pitchClass != '?') {
			int numPitchClasses = PITCH_CLASSES.length();
			int shiftedPitch = PITCH_CLASSES.indexOf(pitchClass) + interval;
			int newIndex = shiftedPitch % numPitchClasses;
			if (newIndex < 0) {
				newIndex += numPitchClasses;
			}
			int octaveShift = shiftedPitch / numPitchClasses;
			if (!register.equals("?")) {
				int newRegister = Integer.parseInt(register);
				newRegister += (shiftedPitch < 0) ? octaveShift - 1 : octaveShift;
				register = String.valueOf(newRegister);
			}
			pitchClass = PITCH_CLASSES.charAt(newIndex);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return pitchClass + REGEX + register;
	}
}
