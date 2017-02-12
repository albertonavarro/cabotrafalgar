package com.navid.trafalgar.mod.counterclock.localfile;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.persistence.CandidateInfo;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.navid.trafalgar.profiles.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class FileRecordPersistenceService implements RecordPersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(FileRecordPersistenceService.class);

    private final Gson gson = new Gson();

    @Autowired
    private ProfileManager profileManager;

    @Override
    public Status getStatus() {
        return Status.OK;
    }

    @Override
    public CandidateInfo addCandidate(CandidateRecord candidateRecord, String sessionId) {

        Qualification currentQualification
                = returnCurrentQualificationForMap(candidateRecord.getHeader().getMap(), candidateRecord.getHeader().getShipModel());

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
        FileWriter fw;
        try {
            fw = new FileWriter(qualification.getFileName());
            gson.toJson(qualification, Qualification.class, fw);
            fw.close();
        } catch (IOException ex) {
            LOG.error("Error saving qualification record with qualification {}", qualification, ex);
            throw new IOError(ex);
        } finally {

        }
    }

    private void saveRecord(CandidateRecord candidateRecord, Qualification qualification) {
        try {
            JsonWriter fw = new JsonWriter(new FileWriter(qualification.getMapName()));
            gson.toJson(candidateRecord, CandidateRecord.class, fw);
            fw.close();
        } catch (IOException ex) {
            LOG.error("Error saving candidate record with qualification {}", qualification, ex);
            throw new IOError(ex);
        }
    }

    private CandidateRecord loadRecord(Qualification qualification) {
        try {
            CandidateRecord candidate;
            JsonReader fr = new JsonReader(new FileReader(qualification.getMapName()));
            candidate = gson.fromJson(fr, qualification.getShipClass());
            fr.close();
            return candidate;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException ex) {
            LOG.error("Error loading candidate record with qualification {}", qualification, ex);
            throw new IOError(ex);
        }
    }

    private File returnFolderForMap(String map, String ship) {

        File shipFolder = new File(profileManager.getHome(), ship);
        if (!shipFolder.exists()) {
            shipFolder.mkdir();
        }

        File currentMapFolder = new File(shipFolder, map.replace("/", "_").replace("\\", "_"));
        if (!currentMapFolder.exists()) {
            currentMapFolder.mkdir();
        }

        return currentMapFolder;
    }

    private CandidateRecord returnGhostForMap(String map, String ship) {
        Qualification q = returnCurrentQualificationForMap(map, ship);
        return loadRecord(q);
    }

    private Qualification returnCurrentQualificationForMap(String map, String ship) {
        File currentMapFolder = returnFolderForMap(map, ship);

        File qualificationFile = new File(currentMapFolder, "ranking.json");
        if (!qualificationFile.exists()) {
            return new Qualification(qualificationFile);
        }

        try {
            Qualification q;
            FileReader fr;
            fr = new FileReader(qualificationFile);
            q = gson.fromJson(fr, Qualification.class);
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

    @Override
    public List<CompetitorInfo> getTopCompetitors(int number, String map, String ship, String sessionId) {
        //TODO use ship
        Qualification q = returnCurrentQualificationForMap(map, ship);

        List<CompetitorInfo> result = new ArrayList<CompetitorInfo>();
        for (int index = 0; index < q.getTimes().size() && index < number; index++) {

            CompetitorInfo currentCompetitor = new CompetitorInfo();
            currentCompetitor.setTime(q.getTimes().get(index));
            currentCompetitor.setLocal(true);
            currentCompetitor.setPosition(index);
            currentCompetitor.setUserName("--yourself--");
            result.add(currentCompetitor);
        }

        return result;

    }

    @Override
    public CandidateRecord getGhost(int number, String map, String ship, String sessionId) {
        //TODO use ship
        if (number == 1) {
            return returnGhostForMap(map, ship);
        }

        throw new UnsupportedOperationException("Getting any ghost position different than 1 is not supported yet.");
    }

    /**
     * @param profileManager the profileManager to set
     */
    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

}
