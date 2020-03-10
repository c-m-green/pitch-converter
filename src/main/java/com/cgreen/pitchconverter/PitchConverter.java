package com.cgreen.pitchconverter;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cgreen.pitchconverter.converter.Decoder;
import com.cgreen.pitchconverter.converter.Encoder;
import com.cgreen.pitchconverter.util.FileWriter;
import com.cgreen.pitchconverter.util.Method;
import com.cgreen.pitchconverter.util.Mode;
import com.cgreen.pitchconverter.util.Params;

import picocli.CommandLine;
import picocli.CommandLine.*;

@Command(name = "PitchConverter", mixinStandardHelpOptions = true, version = "0.6.0")
public class PitchConverter implements Runnable {
    
    private static final Logger LOGGER = LogManager.getLogger();

    // Parameters
    
    @Parameters(arity = "1", index = "0", paramLabel = "INPUT-FILE", description = "File to process. Accepts .txt files.")
    private File input;

    @Parameters(arity = "1", index = "1", paramLabel = "OUTPUT-FILE", description = "Path of output file.")
    private File outputPath;
    
    @Parameters(arity = "0..1", index = "2", paramLabel = "WORD-LIST", description = "File containing a list of valid words to reference when decoding.")
    private File wordCollection;
    
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
    
    @Option(names = {"-g", "--germanH"}, description = "If true, use German H when encoding or decoding by letter." + "\ndefault: false")
    private boolean useGermanH;
    
    @Option(names = {"-e", "--encodingMethod"}, description = "Method of encoding to use for encoding, or to investigate when decoding." + "\nOptions: letter (default), degree")
    private String encodeMethod;
    
    @Option(names = {"-c", "--chromatic"}, description = "If true, use chromatic notes when encoding or decoding by degree." + "\ndefault: false")
    private boolean chromatic;
    
    @Option(names = {"-s", "--stripNonPitchLetters"}, description = "If true, leave out letters that are not pitch classes." + "\ndefault: false")
    private boolean stripNonPitchLetters;

    public void run() {
        if (mode == null) {
            LOGGER.fatal("No mode selected.");
        } else {
            LOGGER.debug("Performing operation...");
            if (performOperation()) {
                LOGGER.info("Operation successful.");
                LOGGER.debug("Returning status code 0...");
                System.exit(0);
            }
        }
        LOGGER.fatal("An error occurred. No output produced.");
        LOGGER.debug("Returning status code 1...");
        System.exit(1);
    }
    
    private boolean performOperation() {
        Params p = Params.getInstance();
        Mode m;
        Method em = getEncodeMethod();
        mode = mode.toLowerCase();
        switch (mode) {
        case "encode":
            m = Mode.ENCODE;
            LOGGER.debug("Calling Encoder");
            return callEncode(p, m, em);
        case "decode":
            m = Mode.DECODE;
            if (wordCollection == null) {
                LOGGER.fatal("No word collection supplied for decoding.");
                break;
            } else {
                LOGGER.debug("Calling Decoder");
                return callDecode(p, m, em, wordCollection);
            }
        default:
            LOGGER.fatal("Invalid mode supplied.");
            break;
        }
        LOGGER.debug("Operation failed.");
        return false;
    }
    
    private boolean callEncode(Params p, Mode m, Method em) {
        p.init(input, m, em, verbose, useGermanH, chromatic, stripNonPitchLetters);
        if (outputFormat == null) {
            // Default to txt
            outputFormat = "text";
        }
        try {
            return FileWriter.writeMusicToFile(Encoder.encodeMessage(p), outputPath, outputFormat);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean callDecode(Params p, Mode m, Method em, File wcFile) {
        p.init(input, m, em, wcFile, verbose, useGermanH, chromatic, stripNonPitchLetters);
        return FileWriter.writeMessagesToFile(Decoder.decodeMessage(p), outputPath);
    }

    private Method getEncodeMethod() {
        if (encodeMethod == null || encodeMethod.isEmpty()) {
            return Method.LETTER;
        } else {
            encodeMethod = encodeMethod.toLowerCase();
            switch(encodeMethod) {
            case "letter":
                return Method.LETTER;
            case "degree":
                return Method.DEGREE;
            default:
                LOGGER.warn("Encoding method not recognized. Defaulting to \"letter.\"");
                return Method.LETTER;
            }
        }
    }
    
    public static void main(String[] args) {
        CommandLine.run(new PitchConverter(), System.out, args);
    }

}
