package com.cgreen.pitchconverter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.audiveris.proxymusic.ScorePartwise;
import org.audiveris.proxymusic.util.Marshalling;
import org.audiveris.proxymusic.util.Marshalling.MarshallingException;

import com.cgreen.pitchconverter.converter.PitchDecoder;
import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;
import com.cgreen.pitchconverter.util.musicxml.PartwiseBuilder;

public final class FileWriter {
    private static final Logger LOGGER = LogManager.getLogger();
    
    //TODO: Don't create a file in multiple places. Do that in one place and get the contents to output depending on outputFormat
    public static boolean writeMusicToFile(List<MusicSymbol> musicOut, File outputFile, String outputFormat) throws IOException {
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
                pw.println(ms.toString() + " " + PitchDecoder.getLabel(ms));
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
    
    private static boolean writeMusicToMusicXml(List<MusicSymbol> musicOut, File outputFile) throws IOException {
        try {
            PartwiseBuilder pb = new PartwiseBuilder(musicOut);
            ScorePartwise score = pb.buildScore(outputFile.getName());
            OutputStream outputStream = new FileOutputStream(outputFile);
            long start = System.currentTimeMillis();
            Marshalling.marshal(score, outputStream, true, null);
            LOGGER.debug("Marshalling done in {} ms", System.currentTimeMillis() - start);
            LOGGER.info("Exported MusicXML score to {}", outputFile.getAbsolutePath());
            outputStream.close();
        } catch (FileNotFoundException | MarshallingException e) {
            LOGGER.fatal("Something went wrong writing to {}", outputFile.getAbsolutePath());
            return false;
        }
        return true;
    }
    
    public static boolean writeMessagesToFile(Set<String> strs, File outputFile) {
        try {
            PrintWriter pw = new PrintWriter(outputFile);
            List<String> perfects = cleanDecodeOutput(strs);
            for (String p : perfects) {
                pw.println(p);
            }
            pw.close();
            System.out.println("Wrote to " + outputFile.getAbsolutePath());
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File not found");
            e.printStackTrace();
        }
        return false;
        
    }
    
    private static List<String> cleanDecodeOutput(Set<String> msgSet) {
        List<String> sortedMsgs = new ArrayList<String>();
        for (String s : msgSet) {
            if (!s.contains("?")) {
                sortedMsgs.add(s);
            }
        }
        sortedMsgs.sort((s1, s2) -> (s1.split(" ").length - s2.split(" ").length));
        return sortedMsgs;
    }
}
