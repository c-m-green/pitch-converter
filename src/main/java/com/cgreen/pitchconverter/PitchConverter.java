package com.cgreen.pitchconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.cgreen.pitchconverter.converter.StringConverter;
import com.cgreen.pitchconverter.datastore.pitch.Pitch;
import com.cgreen.pitchconverter.util.FileReader;
import com.cgreen.pitchconverter.util.FileWriter;

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

	@Option(names = { "--m", "--mode" }, required = true, description = "Select mode to run application. "
			+ "Options: encode, decode")
	private String mode;

	@Option(names = { "-v", "--verbose" }, description = "Verbose mode. Helpful for troubleshooting.")
	private boolean verbose;

	public void run() {
		if (mode == null) {
			System.out.println("Error: Please select a mode to run the application.");
		} else {
			switch (mode) {
			case "encode":
				promptEncode(input, output);
				break;
			case "decode":
				if (wordCollections.length < 1) {
					System.out.println("Error: No word collection provided.");
				} else {
					promptDecode(wordCollections);
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
	
	private static void promptEncode(File input, File output) {
		Scanner s = new Scanner(System.in);
		System.out.println("Select encoding method:");
		System.out.print("1) By letter\n2) By letter, strip non-pitch letters\n3) By letter, use German H (B-natural)\n4) Combine 2 and 3\n5) By degree\n6) By degree, chromatic\n");
		String in = s.nextLine();
		List<Pitch> music = new ArrayList<Pitch>();
		FileReader fileReader = new FileReader();
		String inputText = fileReader.getText(input);
		switch(in) {
		case "1":
			music = StringConverter.byLetter(inputText, false, false);
			break;
		case "2":
			music = StringConverter.byLetter(inputText, true, false);
			break;
		case "3":
			music = StringConverter.byLetter(inputText, false, true);
			break;
		case "4":
			music = StringConverter.byLetter(inputText, true, true);
			break;
		case "5":
			music = StringConverter.byDegree(inputText, 3, false);
			break;
		case "6":
			music = StringConverter.byDegree(inputText, 3, true);
			break;
		default:
			System.out.println("That's not a valid choice...try again.");
			break;
		}
		if (music.size() > 0) {
			FileWriter fileWriter = new FileWriter();
			fileWriter.writeMusicToFile(music, output);
			System.out.println("Wrote to " + output.getAbsolutePath() + "/music.txt");
		}
		s.close();
	}
	
	private static void promptDecode(File[] wordCollections) {
		Scanner s = new Scanner(System.in);
		System.out.println("Sorry, this feature is not quite available yet! Try again later!");
		s.close();
	}

}
