package com.cgreen.pitchconverter;

import java.io.File;

import com.cgreen.pitchconverter.converter.Decoder;
import com.cgreen.pitchconverter.converter.Encoder;
import com.cgreen.pitchconverter.util.FileWriter;
import com.cgreen.pitchconverter.util.Method;
import com.cgreen.pitchconverter.util.Mode;
import com.cgreen.pitchconverter.util.Params;

import picocli.CommandLine;
import picocli.CommandLine.*;

@Command(name = "PitchConverter", mixinStandardHelpOptions = true, version = "0.5.8")
public class PitchConverter implements Runnable {
    
    // Parameters
    
    @Parameters(arity = "1", index = "0", paramLabel = "INPUT-FILE", description = "File to process. Accepts .txt files.")
    private File input;

    @Parameters(arity = "1", index = "1", paramLabel = "OUTPUT-FILE", description = "Path of output file.")
    private File output;
    
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
    @Option(names = { "-o", "--outputFormat"}, description = "Output format of music when encoding." + "\nOptions: text (default)")
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
            System.out.println("Error: Please select a mode to run the application.");
        } else {
            if (performOperation()) {
                System.exit(0);
            }
        }
        System.out.println("An error occurred.");
        System.exit(1);
    }
    
    private boolean performOperation() {
        Params p = Params.getInstance();
        Mode m;
        Method em = getEncodeMethod();
        switch (mode) {
        case "encode":
            m = Mode.ENCODE;
            return callEncode(p, m, em);
        case "decode":
            m = Mode.DECODE;
            if (wordCollection == null) {
                System.out.println("Error: No word collection provided.");
                break;
            } else {
                return callDecode(p, m, em, wordCollection);
            }
        default:
            System.out.println("Error: Invalid mode given.");
            break;
        }
        return false;
    }
    
    private boolean callEncode(Params p, Mode m, Method em) {
        p.init(input, m, em, outputFormat, verbose, useGermanH, chromatic, stripNonPitchLetters);
        return FileWriter.writeMusicToFile(Encoder.encodeMessage(p), output);
    }
    
    private boolean callDecode(Params p, Mode m, Method em, File wcFile) {
        p.init(input, m, em, wcFile, verbose, useGermanH, chromatic, stripNonPitchLetters);
        return FileWriter.writeMessagesToFile(Decoder.decodeMessage(p), output);
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

}
