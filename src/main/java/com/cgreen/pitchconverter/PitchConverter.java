package com.cgreen.pitchconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "PitchConverter", mixinStandardHelpOptions = true, version = "trial 0.3.1")
public class PitchConverter implements Runnable {

	@Parameters(arity = "1", paramLabel = "INPUT-FILE", description = "File to process.")
	private File input;

	@Parameters(arity = "1", paramLabel = "OUTPUT-FILE", description = "Path of output file.")
	private File output;
	
	@Parameters(arity = "0..*", paramLabel = "WORD-LISTS", description = "File(s) each containing a list of valid words to reference when decoding.")
	private File[] wordCollections;

	@Option(names = { "--m", "--mode" }, required = true, description = "Select mode to run application. "
			+ "Options: encode, decode")
	private String mode;

	@Option(names = { "-v", "--verbose" }, description = "Verbose mode. Helpful for troubleshooting.")
	private boolean verbose;

	public void run() {
		if (mode == null) {
			System.out.println("Please select a mode to run the application.");
		} else {
			switch (mode) {
			case "encode":
				promptEncode();
				break;
			case "decode":
				if (wordCollections.length < 1) {
					System.out.println("Error: No word collection provided.");
				} else {
					promptDecode();
				}
				break;
			default:
				System.out.println("Error: Invalid mode given.");
				break;
			}
		}
	}

	public static void main(String[] args) {
		CommandLine.run(new PitchConverter(), System.out, args);
	}
	
	private static void promptEncode() {
		Scanner s = new Scanner(System.in);
		s.close();
	}
	
	private static void promptDecode() {
		Scanner s = new Scanner(System.in);
		s.close();
	}

}
