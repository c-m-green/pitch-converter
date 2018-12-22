package com.cgreen.pitchconverter.datastore.pitch;

public final class SymbolFactory {
	static final String PITCH_CLASS_INTS = "0123456789te";
	// TODO: @returns for these methods in the javadoc comments are inaccurate!!!
	/**
	 * Create a Pitch.
	 * 
	 * @param pitchClass - an integer representation of a pitch class. 't' and 'e'
	 *                   are used for B-flat and B-natural, respectively.
	 * @return a Pitch of the given pitch class. Invalid pitch classes return a
	 *         pitch of '?'.
	 */
	public static MusicSymbol createSymbol(char pitchClass) {
		if (isValidPitchClass(pitchClass)) {
			return new Pitch(pitchClass);
		} else {
			return new Rest();
		}
	}
	
	/**
	 * Create a Pitch in a particular register.
	 * 
	 * @param pitchClass
	 * @param register
	 * @return a Pitch of the given pitch class. Invalid pitch classes return a
	 *         pitch of '?'.
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
