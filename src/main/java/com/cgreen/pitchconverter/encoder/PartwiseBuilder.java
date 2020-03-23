package com.cgreen.pitchconverter.encoder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.audiveris.proxymusic.Attributes;
import org.audiveris.proxymusic.Clef;
import org.audiveris.proxymusic.ClefSign;
import org.audiveris.proxymusic.Key;
import org.audiveris.proxymusic.Note;
import org.audiveris.proxymusic.NoteType;
import org.audiveris.proxymusic.ObjectFactory;
import org.audiveris.proxymusic.Opus;
import org.audiveris.proxymusic.PartList;
import org.audiveris.proxymusic.PartName;
import org.audiveris.proxymusic.ScorePart;
import org.audiveris.proxymusic.ScorePartwise;
import org.audiveris.proxymusic.ScorePartwise.Part.Measure;
import org.audiveris.proxymusic.Step;
import org.audiveris.proxymusic.Time;
import org.audiveris.proxymusic.Work;

import com.cgreen.pitchconverter.datastore.pitch.MusicSymbol;

public class PartwiseBuilder {
    private List<MusicSymbol> music;
    PartwiseBuilder(List<MusicSymbol> music) {
        this.music = music;
    }
    
    ScorePartwise buildScore(String title) {
        // https://github.com/Audiveris/proxymusic/blob/master/src/test/java/org/audiveris/proxymusic/util/HelloWorldTest.java#L344
        //TODO: Handle case of empty music
        ObjectFactory factory = new ObjectFactory();
        ScorePartwise scorePartwise = factory.createScorePartwise();
        
        // Work
        Work work = factory.createWork();
        scorePartwise.setWork(work);
        work.setWorkTitle(title);
        work.setWorkNumber("Number for the work");
        
        /*
        // Work::Opus
        Opus opus = factory.createOpus();
        work.setOpus(opus);
        opus.setHref("Href to opus");
        opus.setType("simple");
        opus.setRole("Role of opus"); // Some text
        opus.setTitle("Opus for " + title); // Some text
        opus.setShow("new"); // new, replace, embed, other, none
        opus.setActuate("none"); // onRequest, onLoad, other, none
        */
        
        // PartList
        PartList partList = factory.createPartList();
        scorePartwise.setPartList(partList);

        // Scorepart in partList
        ScorePart scorePart = factory.createScorePart();
        partList.getPartGroupOrScorePart().add(scorePart);
        scorePart.setId("P1");
        
        PartName partName = factory.createPartName();
        scorePart.setPartName(partName);
        partName.setValue("Music");

        // ScorePart in scorePartwise
        ScorePartwise.Part part = factory.createScorePartwisePart();
        scorePartwise.getPart().add(part);
        part.setId(scorePart);

        

        // Attributes
        Attributes attributes = factory.createAttributes();

        // Divisions
        attributes.setDivisions(new BigDecimal(1));

        // Key
        Key key = factory.createKey();
        attributes.getKey().add(key);
        key.setFifths(new BigInteger("0"));

        // Time
        Time time = factory.createTime();
        attributes.getTime().add(time);
        time.getTimeSignature().add(factory.createTimeBeats("4"));
        time.getTimeSignature().add(factory.createTimeBeatType("4"));

        // Clef
        Clef clef = factory.createClef();
        attributes.getClef().add(clef);
        clef.setSign(ClefSign.G);
        clef.setLine(new BigInteger("2"));
        
        Map<Integer, Step> pitchToStepMap = matchPitchesToSteps();
        int beat = 0;
        int barNumber = 1;
        Measure measure = factory.createScorePartwisePartMeasure();
        part.getMeasure().add(measure);
        measure.setNumber(barNumber + "");
        measure.getNoteOrBackupOrForward().add(attributes);
        
        for (MusicSymbol ms : music) {
            // Create an appropriate object
            // Add to the score
            beat++;
            if (beat > 4) {
                beat = 1;
                barNumber++;
                measure = factory.createScorePartwisePartMeasure();
                part.getMeasure().add(measure);
                measure.setNumber(barNumber + "");
            }
                                    
            if (ms.getPitchClass() == 'r') {
                Note note = factory.createNote();
                measure.getNoteOrBackupOrForward().add(note);
                org.audiveris.proxymusic.Rest rest = factory.createRest();
                note.setRest(rest);
                
                // Type
                NoteType type = factory.createNoteType();
                type.setValue("quarter");
                note.setType(type);
            } else {
                // Note
                Note note = factory.createNote();
                measure.getNoteOrBackupOrForward().add(note);
                com.cgreen.pitchconverter.datastore.pitch.Pitch p = (com.cgreen.pitchconverter.datastore.pitch.Pitch) ms;
                // Pitch
                org.audiveris.proxymusic.Pitch pitch = factory.createPitch();
                note.setPitch(pitch);
                pitch.setStep(pitchToStepMap.get(p.getPitchClassAsInt()));
                if (!p.isNatural()) {
                    pitch.setAlter(new BigDecimal(1));
                }
                pitch.setOctave(Integer.parseInt(p.getRegister()));
                // Duration
                note.setDuration(new BigDecimal(1));
                
                // Type
                NoteType type = factory.createNoteType();
                type.setValue("quarter");
                note.setType(type);
            }
        }                
        return scorePartwise;
    }
    
    private Map<Integer, Step> matchPitchesToSteps() {
        Map<Integer, Step> pitchMap = new HashMap<Integer, Step>();
        pitchMap.put(0, Step.C);
        pitchMap.put(1, Step.C);
        pitchMap.put(2, Step.D);
        pitchMap.put(3, Step.D);
        pitchMap.put(4, Step.E);
        pitchMap.put(5, Step.F);
        pitchMap.put(6, Step.F);
        pitchMap.put(7, Step.G);
        pitchMap.put(8, Step.G);
        pitchMap.put(9, Step.A);
        pitchMap.put(10, Step.A);
        pitchMap.put(11, Step.B);
        return pitchMap;
    }
}
