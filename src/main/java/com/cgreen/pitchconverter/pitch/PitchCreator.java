package com.cgreen.pitchconverter.pitch;

public class PitchCreator {
	private static final String PITCH_CLASS_INTS = "0123456789te";
	
	public static Pitch createPitch(char pitchClass) {
		if (isValidPitchClass(pitchClass)) {
			return new Pitch(pitchClass);
		} else {
			return new Pitch('?');
		}
	}
	
	public static Pitch createPitch(char pitchClass, int register) {
		if (isValidPitchClass(pitchClass)) {
			return new Pitch(pitchClass, register);
		} else {
			return new Pitch('?');
		}
	}
	
	private static boolean isValidPitchClass(char pc) {
		return !(PITCH_CLASS_INTS.indexOf(pc) == -1);
	}
	// TODO: Test this
	public static Pitch transposePitch(Pitch p, int interval) {
		char pc = p.getPitchClass();
		String r = p.getRegister();
		if (pc != '?') {
			int numPitchClasses = PITCH_CLASS_INTS.length();
			int shiftedPitch = PITCH_CLASS_INTS.indexOf(pc) + interval;
			int newIndex = shiftedPitch % numPitchClasses;
			if (newIndex < 0) {
				newIndex += numPitchClasses;
			}
			int octaveShift = shiftedPitch / numPitchClasses;
			// TODO: What this currently does with r is clunky converting from int to String to int. Consider refinement.
			if (!r.equals("?")) {
				int newRegister = Integer.parseInt(r);
				newRegister += (shiftedPitch < 0) ? octaveShift - 1 : octaveShift;
				r = String.valueOf(newRegister);
			}
			char newPc = PITCH_CLASS_INTS.charAt(newIndex);
			return createPitch(newPc, Integer.parseInt(r));
		}
		return p;
	}
}
