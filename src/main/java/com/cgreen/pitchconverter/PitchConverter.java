package com.cgreen.pitchconverter;

import java.io.File;
import java.util.Scanner;

import com.cgreen.pitchconverter.converter.Encoder;
import com.cgreen.pitchconverter.util.FileWriter;
import com.cgreen.pitchconverter.util.Method;
import com.cgreen.pitchconverter.util.Mode;
import com.cgreen.pitchconverter.util.Params;

import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "PitchConverter", mixinStandardHelpOptions = true, version = "0.5.0")
public class PitchConverter implements Runnable {

	@Parameters(arity = "1", paramLabel = "INPUT-FILE", description = "File to process.")
	private File input;

	@Parameters(arity = "1", paramLabel = "OUTPUT-FILE", description = "Path of output file.")
	private File output;
	
	@Parameters(arity = "0..*", paramLabel = "WORD-LISTS", description = "File(s) each containing a list of valid words to reference when decoding.")
	private File[] wordCollections;

	@Option(names = { "--m", "--mode" }, required = true, description = "Select mode to run application."
			+ "\nOptions: encode, decode")
	private String mode;

	@Option(names = { "-v", "--verbose" }, description = "Verbose mode. Helpful for troubleshooting.")
	private boolean verbose;
	
	// TODO: Output MIDI
	@Option(names = { "-o", "--outputFormat"}, description = "Output format of music when encoding." + "\nOptions: text (default)")
	private String outputFormat;
	
	@Option(names = {"-g", "--germanH"}, description = "If true, use German H when encoding or decoding by letter." + "\ndefault: false")
	private boolean useGermanH;
	
	@Option(names = {"-e", "--encodingMethod"}, description = "Method of encoding to use for encoding or decoding purposes." + "\nOptions: letter (default), degree")
	private String encodeMethod;
	
	@Option(names = {"-c", "--chromatic"}, description = "If true, use chromatic notes when encoding or decoding by degree." + "\ndefault: false")
	private boolean chromatic;
	
	@Option(names = {"-s", "--stripNonPitchLetters"}, description = "If true, leave out letters that are not pitch classes." + "\ndefault: false")
	private boolean stripNonPitchLetters;

	public void run() {
		// TODO: Handle FileNotFoundException here
		Params p = Params.getInstance();
		Mode m;
		Method em = getEncodeMethod();
		
		if (mode == null) {
			System.out.println("Error: Please select a mode to run the application.");
			System.exit(1);
		} else {
			switch (mode) {
			case "encode":
				m = Mode.ENCODE;
				p.init(input, m, em, outputFormat, verbose, useGermanH, chromatic, stripNonPitchLetters);
				FileWriter.writeMusicToFile(Encoder.encodeMessage(p), output);
				break;
			case "decode":
				m = Mode.DECODE;
				if (wordCollections.length < 1) {
					System.out.println("Error: No word collection provided.");
				} else {
					promptDecode(wordCollections);
				}
				break;
			default:
				System.out.println("Error: Invalid mode given.");
				System.exit(1);
			}
		}
		System.exit(0);
	}

	private Method getEncodeMethod() {
		if (encodeMethod == null || encodeMethod.isEmpty()) {
			return Method.LETTER;
		} else {
			switch(encodeMethod) {
			case "letter":
				return Method.LETTER;
			case "degree":
				return Method.DEGREE;
			default:
				System.out.println("Encoding method not recognized. Defaulting to \"letter\"...");
				return Method.LETTER;
			}
		}
	}

	public static void main(String[] args) {
		CommandLine.run(new PitchConverter(), System.out, args);
	}
	
	private static void promptDecode(File[] wordCollections) {
		Scanner s = new Scanner(System.in);
		System.out.println("Sorry, this feature is not quite available yet! Try again later!");
		s.close();
	}

}
