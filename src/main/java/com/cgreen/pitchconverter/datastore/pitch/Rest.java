package com.cgreen.pitchconverter.datastore.pitch;

public class Rest extends MusicSymbol {
	Rest() { }

	@Override
	public char getPitchClass() {
		return '?';
	}

	@Override
	public String getRegister() {
		return "N/A";
	}

	@Override
	public String toString() {
		return "[rest]";
	}
}
