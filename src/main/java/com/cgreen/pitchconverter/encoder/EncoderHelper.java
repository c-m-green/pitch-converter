package com.cgreen.pitchconverter.encoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.audiveris.proxymusic.ScorePartwise;
import org.audiveris.proxymusic.util.Marshalling;
import org.audiveris.proxymusic.util.Marshalling.MarshallingException;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.util.Params;

public final class EncoderHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static String getText(File file) throws FileNotFoundException {
        if (!isValidInputFile(file)) {
            throw new FileNotFoundException("The input file was inaccessible.");
        }
        StringBuilder textLines = new StringBuilder();
        Scanner s = new Scanner(file);
        while(s.hasNextLine()) {
            textLines.append(s.nextLine());
        }
        s.close();
        return textLines.toString();
    }
    
    static List<MusicSymbol> createMusic(File inputFile, Params p) throws FileNotFoundException {
        List<MusicSymbol> music = new ArrayList<MusicSymbol>();
        String message = EncoderHelper.getText(inputFile);
        if (message == null || message.isEmpty() || message.isBlank()) {
            LOGGER.debug("No text from the input file was found.");
            return music;
        }
        int startOctave;
        switch(p.getMethod()) {
        case LETTER:
            startOctave = p.getStripLetters() ? 4 : 3;
            music = StringConverter.byLetter(message, startOctave, p.getStripLetters(), p.getUseGermanH(), p.getIncludeRests());
            break;
        case DEGREE:
            startOctave = p.isChromatic() ? 4 : 3;
            music = StringConverter.byDegree(message, startOctave, p.isChromatic(), p.getIncludeRests());
            break;
        default:
            LOGGER.info("Invalid encoding method value. No conversion was performed.");
            break;
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
    
    static void writeMusicToFile(List<MusicSymbol> musicOut, File outputFile, String outputFormat) {
        //TODO: Change outputFormat to enum and modify this part.
        outputFormat = outputFormat.toLowerCase();
        // TODO: Check for ability to write to output file
        switch(outputFormat) {
        case "text":
            writeMusicToTxt(musicOut, outputFile);
            break;
        case "musicxml":
            writeMusicToMusicXml(musicOut, outputFile);
            break;
        case "midi":
            //writeMusicToMidi(musicOut, outputFile);
            //break;
        default:
            LOGGER.fatal("Output format \"{}\" is invalid.", outputFormat);
            throw new IllegalArgumentException("The output format was invalid.");
        }
    }
    
    private static boolean writeMusicToTxt(List<MusicSymbol> musicOut, File outputFile) {
        try {
            PrintWriter pw = new PrintWriter(outputFile.getAbsolutePath());
            for (MusicSymbol ms : musicOut) {
                pw.println(ms);
            }
            pw.close();
            LOGGER.info("Wrote to " + outputFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            LOGGER.error("File not found.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private static boolean writeMusicToMusicXml(List<MusicSymbol> musicOut, File outputFile) {
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
            return false;
        } catch (MarshallingException me) {
            LOGGER.fatal("Something went wrong marshalling into a score.");
            return false;
        }
        return true;
    }
    
    private static boolean isValidInputFile(File inputFile) {
        return inputFile.isFile() && (getFileExtension(inputFile).equals("txt"));
    }
}
