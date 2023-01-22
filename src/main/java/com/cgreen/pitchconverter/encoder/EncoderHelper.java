package com.cgreen.pitchconverter.encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.audiveris.proxymusic.ScorePartwise;
import org.audiveris.proxymusic.util.Marshalling;
import org.audiveris.proxymusic.util.Marshalling.MarshallingException;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.util.OutputFormat;
import com.cgreen.pitchconverter.util.Params;

public final class EncoderHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    private static String getText(File file) throws IOException {
        if (!isValidInputFile(file)) {
            throw new FileNotFoundException("The input file was inaccessible.");
        }
        StringBuilder textLines = new StringBuilder();
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        String line = "";
        while (line != null) {
            line = lnr.readLine();
            if (line != null) {
                if (lnr.getLineNumber() > 1) {
                    textLines.append("\n");
                }
                textLines.append(line);
            }
        }
        return textLines.toString();
    }

    static List<MusicSymbol> createMusic(File inputFile, Params p) throws IOException {
        String fileContents = EncoderHelper.getText(inputFile);
        return convertStringToMusic(fileContents, p);
    }
    
    static List<MusicSymbol> convertStringToMusic(String input, Params p) {
        List<MusicSymbol> music = new ArrayList<MusicSymbol>();
        if (input == null || input.isEmpty() || input.isBlank()) {
            LOGGER.debug("No text from the input file was found.");
            return music;
        }
        int startOctave;
        switch (p.getMethod()) {
            case LETTER -> {
                startOctave = p.getStripLetters() ? 4 : 3;
                music = StringConverter.byLetter(input, startOctave, p.getStripLetters(), p.getUseGermanH(), p.getIncludeRests());
            }
            case DEGREE -> {
                startOctave = p.isChromatic() ? 4 : 3;
                music = StringConverter.byDegree(input, startOctave, p.isChromatic(), p.getIncludeRests());
            }
            default -> LOGGER.info("Invalid encoding method value. No conversion was performed.");
        }
        return music;
    }
    
    private static String getFileExtension(File file) {
        String extension = "";
        String fileName = file.getAbsolutePath();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }
    
    static void writeMusicToFile(List<MusicSymbol> musicOut, File outputFile, OutputFormat outputFormat) throws FileNotFoundException {
        // TODO: Check for ability to write to output file
        switch(outputFormat) {
        case TEXT:
            writeMusicToTxt(musicOut, outputFile);
            break;
        case MUSICXML:
            writeMusicToMusicXml(musicOut, outputFile);
            break;
        case MIDI:
            //writeMusicToMidi(musicOut, outputFile);
            //break;
        default:
            LOGGER.debug("Output format \"{}\" is invalid.", outputFormat);
            throw new IllegalArgumentException("Invalid output format: " + outputFormat);
        }
    }
    
    private static void writeMusicToTxt(List<MusicSymbol> musicOut, File outputFile) throws FileNotFoundException {
        //TODO: try-with-resources
        PrintWriter pw = new PrintWriter(outputFile.getAbsolutePath());
        for (MusicSymbol ms : musicOut) {
            pw.println(ms);
        }
        pw.close();
        LOGGER.info("Wrote to " + outputFile.getAbsolutePath());
    }
    
    private static void writeMusicToMusicXml(List<MusicSymbol> musicOut, File outputFile) {
        //TODO: try-with-resources
        try {
            PartwiseBuilder pb = new PartwiseBuilder(musicOut);
            ScorePartwise score = pb.buildScore(outputFile.getName());
            OutputStream outputStream = new FileOutputStream(outputFile);
            long start = System.currentTimeMillis();
            Marshalling.marshal(score, outputStream, true, null);
            LOGGER.debug("Marshalling done in {} ms", System.currentTimeMillis() - start);
            LOGGER.info("Exported MusicXML score to {}", outputFile.getAbsolutePath());
            outputStream.close();
        } catch (IOException ioe) {
            LOGGER.fatal("Something went wrong writing to {}", outputFile.getAbsolutePath());
        } catch (MarshallingException me) {
            LOGGER.fatal("Something went wrong marshalling into a score.");
        }
    }
    
    private static boolean isValidInputFile(File inputFile) {
        return inputFile.isFile() && (getFileExtension(inputFile).equals("txt"));
    }
}
