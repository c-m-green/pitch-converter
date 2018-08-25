package com.cgreen.pitchconverter;

import java.util.HashMap;
import java.util.Map;

public class Pitch {
	protected static final String[] PITCH_CLASS_LABELS = {"C-natural","C-sharp/D-flat","D-natural","D-sharp/E-flat","E-natural","F-natural","F-sharp/G-flat","G-natural","G-sharp/A-flat","A-natural","A-sharp/B-flat","B-natural"};
	private static final String PITCH_CLASS_INTS = "0123456789te";
	private static final String REGEX = ":";
	private char pitchClass;
	private String register;
	private static final String BLANK = "???";

	public Pitch(char pitchClass) {
		this.pitchClass = '?';
		if (PITCH_CLASS_INTS.indexOf(pitchClass) != -1) {
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
			int numPitchClasses = PITCH_CLASS_INTS.length();
			int shiftedPitch = PITCH_CLASS_INTS.indexOf(pitchClass) + interval;
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
			pitchClass = PITCH_CLASS_INTS.charAt(newIndex);
			return true;
		}
		return false;
	}
	
	public String getLabel() {
		int pitchIndex = getIntRepresentation(getPitchClass());
		try {
			String out = PITCH_CLASS_LABELS[pitchIndex];
			return out;
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			// System.out.println("Got symbol: " + p.getPitchClass());
			return BLANK;
		}
	}
	
	protected static int getIntRepresentation(char arg) {
		int i;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("t", 10);
		map.put("e", 11);
		try {
			i = Integer.parseInt(arg + "");
		} catch (NumberFormatException nfe) {
			// System.out.println(arg + " not parsed -- searching map...");
			try {
				i = map.get(arg + "");
				// System.out.println("Got it!");
			} catch (NullPointerException npe) {
				// System.out.println("Value not found in map: " + arg);
				return -1;
			}
		}
		return i;
	}
	
	@Override
	public String toString() {
		return pitchClass + REGEX + register;
	}
}
