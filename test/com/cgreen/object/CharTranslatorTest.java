package com.cgreen.object;

import junit.framework.TestCase;

public class CharTranslatorTest extends TestCase {
	protected char charPitch, charNonPitch, charH, charB, charNum, charSymb, charQ, charSpace;
	protected Character charNull, charMin;
	
	public void setUp() {
		charPitch = 'a';
		charNonPitch = 'w';
		charH = 'h';
		charB = 'b';
		charNum = '7';
		charSymb = '@';
		charQ = '?';
		charSpace = ' ';
		charNull = null;
		charMin = Character.MIN_VALUE;
	}
	
	public void testLetterToPitchLiteralUsingGerman() {
		
	}
}
