package com.navid.trafalgar.persistence.localfile;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.navid.trafalgar.persistence.CandidateInfo;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anf
 */
public class FileRecordPersistenceService implements RecordPersistenceService {

    Gson gson = new Gson();

    public CandidateInfo addCandidate(CandidateRecord candidateRecord) {

        Qualification currentQualification = returnCurrentQualificationForMap(candidateRecord.getHeader().getMap());

        int index;
        for (index = 0; index < currentQualification.getTimes().size(); index++) {
            if (currentQualification.getTimes().get(index) > candidateRecord.getTime()) {
                break;
            }
        }

        if (index == 0) {
            currentQualification.setShipClass(candidateRecord.getClass());
        }

        currentQualification.getTimes().add(index, candidateRecord.getTime());
        saveQualification(currentQualification);

        if (index == 0) {
            saveRecord(candidateRecord, currentQualification);
        }

        CandidateInfo candidateInfo = new CandidateInfo();
        candidateInfo.setPosition(index);
        return candidateInfo;
    }

    private void saveQualification(Qualification qualification) {
        try {
            FileWriter fw = new FileWriter(qualification.getFileName());
            gson.toJson(qualification, Qualification.class, fw);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(FileRecordPersistenceService.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOError(ex);
        }
    }

    private void saveRecord(CandidateRecord candidateRecord, Qualification qualification) {
        try {
            JsonWriter fw = new JsonWriter(new FileWriter(qualification.getMapName()));
            gson.toJson(candidateRecord, CandidateRecord.class, fw);
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(FileRecordPersistenceService.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOError(ex);
        }
    }

    private CandidateRecord loadRecord(Qualification qualification) {
        try {
            JsonReader fr = new JsonReader(new FileReader(qualification.getMapName()));
            CandidateRecord candidate = gson.fromJson(fr, qualification.getShipClass());
            fr.close();
            return candidate;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException ex) {
            Logger.getLogger(FileRecordPersistenceService.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOError(ex);
        }
    }

    private File returnFolderForMap(String map) {
        String userHome = System.getProperty("user.home");

        File userHomeFile = new File(userHome);

        File trafalgarFolder = new File(userHomeFile, ".cabotrafalgar");

        if (!trafalgarFolder.exists()) {
            trafalgarFolder.mkdir();
        }

        File mapsFolder = new File(trafalgarFolder, "maps");
        if (!mapsFolder.exists()) {
            mapsFolder.mkdir();
        }

        File currentMapFolder = new File(mapsFolder, map.replace("/", "_").replace("\\", "_"));
        if (!currentMapFolder.exists()) {
            currentMapFolder.mkdir();
        }

        return currentMapFolder;
    }

    private CandidateRecord returnGhostForMap(String map) {

        Qualification q = returnCurrentQualificationForMap(map);

        return loadRecord(q);

    }

    private Qualification returnCurrentQualificationForMap(String map) {

        File currentMapFolder = returnFolderForMap(map);

        File qualificationFile = new File(currentMapFolder, "ranking.json");
        if (!qualificationFile.exists()) {
            return new Qualification(qualificationFile);
        }

        try {
            FileReader fr = new FileReader(qualificationFile);
            Qualification q = gson.fromJson(fr, Qualification.class);
            fr.close();
            if (q == null) {
                return new Qualification(qualificationFile);
            }
            return q;
        } catch (FileNotFoundException ex) {
            return new Qualification(qualificationFile);
        } catch (IOException e2) {
            throw new IOError(e2);
        }


    }

    public List<CompetitorInfo> getTopCompetitors(int number, String map) {
        Qualification q = returnCurrentQualificationForMap(map);

        List<CompetitorInfo> result = new ArrayList<CompetitorInfo>();
        for (int index = 0; index < q.getTimes().size() && index < number; index++) {

            CompetitorInfo currentCompetitor = new CompetitorInfo();
            currentCompetitor.setTime(q.getTimes().get(index));
            currentCompetitor.setLocal(true);
            currentCompetitor.setPosition(index);
            result.add(currentCompetitor);
        }

        return result;

    }

    public CandidateRecord getGhost(int number, String map) {
        if (number == 1) {
            return returnGhostForMap(map);
        }

        throw new UnsupportedOperationException("Getting any ghost position different than 1 is not supported yet.");
    }
}
