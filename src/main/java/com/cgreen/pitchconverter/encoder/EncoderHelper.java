package com.cgreen.pitchconverter.encoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.audiveris.proxymusic.ScorePartwise;
import org.audiveris.proxymusic.util.Marshalling;
import org.audiveris.proxymusic.util.Marshalling.MarshallingException;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;

public final class EncoderHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    static String getText(File file) {
        StringBuilder textLines = new StringBuilder();
        try {
            if (!getFileExtension(file).equals("txt")) {
                return "";
            }
            Scanner s = new Scanner(file);
            while(s.hasNextLine()) {
                textLines.append(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: The input file " + file.getAbsolutePath() + " was not found!");
            System.exit(1);
        }
        return textLines.toString();
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
    
    //TODO: Don't create a file in multiple places. Do that in one place and get the contents to output depending on outputFormat
    static boolean writeMusicToFile(List<MusicSymbol> musicOut, File outputFile, String outputFormat) {
        boolean isSuccess = false;
        outputFormat = outputFormat.toLowerCase();
        switch(outputFormat) {
        case "text":
            isSuccess = writeMusicToTxt(musicOut, outputFile);
            break;
        case "musicxml":
            isSuccess = writeMusicToMusicXml(musicOut, outputFile);
            break;
        case "midi":
            //isSuccess = writeMusicToMidi(musicOut, outputFile);
            //break;
        default:
            LOGGER.fatal("Output format \"{}\" is invalid.", outputFormat);
            return false;
        }
        return isSuccess;        
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
}
