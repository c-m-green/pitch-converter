package com.cgreen.pitchconverter.pitch;

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
