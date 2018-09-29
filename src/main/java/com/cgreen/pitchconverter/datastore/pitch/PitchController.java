package com.cgreen.pitchconverter.datastore.pitch;

public class PitchController {
	public static MusicSymbol transposePitch(MusicSymbol p, int interval) {
		char pc = p.getPitchClass();
		String r = p.getRegister();
		if (pc != '?') {
			int numPitchClasses = SymbolFactory.PITCH_CLASS_INTS.length();
			int shiftedPitch = SymbolFactory.PITCH_CLASS_INTS.indexOf(pc) + interval;
			int newIndex = shiftedPitch % numPitchClasses;
			if (newIndex < 0) {
				newIndex += numPitchClasses;
			}
			int octaveShift = shiftedPitch / numPitchClasses;
			if (!r.equals("?") && !r.equals("N/A")) {
				int newRegister = Integer.parseInt(r);
				newRegister += (shiftedPitch < 0) ? octaveShift - 1 : octaveShift;
				r = String.valueOf(newRegister);
			}
			char newPc = SymbolFactory.PITCH_CLASS_INTS.charAt(newIndex);
			return SymbolFactory.createSymbol(newPc, Integer.parseInt(r));
		}
		return p;
	}
}
