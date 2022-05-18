package com.cgreen.pitchconverter;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.decoder.Decoder;
import com.cgreen.pitchconverter.encoder.Encoder;
import com.cgreen.pitchconverter.util.Method;
import com.cgreen.pitchconverter.util.OutputFormat;
import com.cgreen.pitchconverter.util.Params;

import picocli.CommandLine;
import picocli.CommandLine.*;

@Command(name = "PitchConverter", mixinStandardHelpOptions = true, version = "0.10.1")
public class PitchConverter implements Runnable {
    
    private static final Logger LOGGER = LogManager.getLogger();

    // Parameters
    
    @Parameters(arity = "1", index = "0", paramLabel = "INPUT-FILE", description = "File to process. Accepts .txt files.")
    private File inputPath;

    @Parameters(arity = "1", index = "1", paramLabel = "OUTPUT-FILE", description = "Path of output file.")
    private File outputPath;
    
    // Required options
    
    @Option(names = { "-m", "--mode" }, required = true, description = "Select mode to run application."
            + "\nOptions: encode, decode")
    private String mode;
    
    // Options

    @Option(names = { "-v", "--verbose" }, description = "Verbose mode. Helpful for troubleshooting.")
    private boolean verbose;
    
    // TODO: Output MIDI
    @Option(names = { "-o", "--outputFormat"}, description = "Output format of music when encoding." + "\nOptions: text (default), musicxml")
    private String outputFormat;
    
    @Option(names = {"-g", "--germanH"}, description = "Use German H when encoding or decoding by letter." + "\nBy default, the letter B will convert to B-natural.")
    private boolean useGermanH;
    
    @Option(names = {"-e", "--encodingMethod"}, description = "Method of encoding to use for encoding, or to investigate when decoding." + "\nOptions: letter (default), degree")
    private String encodeMethod;
    
    @Option(names = {"-c", "--chromatic"}, description = "Use chromatic notes when encoding or decoding by degree." + "\nThe notes of the C major scale are used by default.")
    private boolean chromatic;
    
    @Option(names = {"-s", "--stripNonPitchLetters"}, description = "Leave out letters that are not pitch classes." + "\nAll letters are converted by default.")
    private boolean stripNonPitchLetters;
    
    @Option(names = {"-r", "--includeRests"}, description = "Convert spaces, punctuation, and new lines to rests." + "\nAll spaces and non-alphanumeric characters are ignored by default.")
    private boolean includeRests;
    
    @Option(names = {"-w", "--wordList"}, description = "Option to override the default English dictionary with a user-supplied text file.")
    private File wordCollectionPath;

    public void run() {
        LOGGER.debug("Performing operation...");
        try {
            performOperation();
            LOGGER.info("Operation successful.");
            LOGGER.debug("Returning status code 0...");
            System.exit(0);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            LOGGER.debug("An error occurred. No output produced.");
            LOGGER.fatal("ERROR: " + e.getMessage());
            LOGGER.debug("Returning status code 1...");
            System.exit(1);
        }
    }
    
    private void performOperation() throws FileNotFoundException {
        Method em = getEncodeMethod();
        Params p = new Params(em, useGermanH, chromatic, stripNonPitchLetters, includeRests);
        mode = mode.toLowerCase();
        switch (mode) {
        case "encode":
            LOGGER.debug("Calling Encoder");
            callEncode(p);
            break;
        case "decode":
            LOGGER.debug("Calling Decoder");
            callDecode(p);
            break;
        default:
            LOGGER.debug("Invalid mode supplied.");
            throw new IllegalArgumentException("Neither mode 'encode' nor 'decode' was selected.");
        }
    }
    
    private void callEncode(Params p) throws FileNotFoundException {
        if (wordCollectionPath != null) {
            LOGGER.info("Word list param will be ignored for encoding.");
        }
        Encoder encoder = new Encoder(p);
        encoder.encodeMessageToFile(inputPath, outputPath, getOutputFormat());
    }
    
    private void callDecode(Params p) throws FileNotFoundException {
        if (!(outputFormat == null && outputFormat.isEmpty())) {
            LOGGER.info("Output format param will be ignored for decoding.");
        }
        Decoder decoder = new Decoder();
        decoder.decodeMessageToFile(inputPath, outputPath, wordCollectionPath, p);
    }

    private Method getEncodeMethod() {
        if (encodeMethod == null || encodeMethod.isEmpty()) {
            LOGGER.debug("Defaulting to \"letter\" encoding");
            return Method.LETTER;
        } else {
            encodeMethod = encodeMethod.toLowerCase();
            switch(encodeMethod) {
            case "letter":
                return Method.LETTER;
            case "degree":
                return Method.DEGREE;
            default:
                LOGGER.warn("Invalid encoding method \"" + encodeMethod + "\" received.");
                return Method.INVALID;
            }
        }
    }
    
    private OutputFormat getOutputFormat() {
        if (outputFormat == null || outputFormat.isEmpty()) {
            LOGGER.debug("Defaulting to \"text\" output format.");
            return OutputFormat.TEXT;
        } else {
            outputFormat = outputFormat.toLowerCase();
            switch(outputFormat) {
            case "text":
                return OutputFormat.TEXT;
            case "musicxml":
                return OutputFormat.MUSICXML;
            case "midi":
                return OutputFormat.MIDI;
            default:
                LOGGER.warn("Invalid outputFormat \"" + outputFormat + "\" received.");
                return OutputFormat.INVALID;
            }
        }
    }
    
    public static void main(String[] args) {
        CommandLine.run(new PitchConverter(), System.out, args);
    }
}
