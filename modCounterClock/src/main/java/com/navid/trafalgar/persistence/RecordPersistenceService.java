package com.navid.trafalgar.persistence;

import com.navid.trafalgar.model.CandidateRecord;
import java.util.List;

/**
 *
 * @author alberto
 */
public interface RecordPersistenceService {

    /**
     * This method asks for recording the current result. 
     * Recording is not guaranteed as system could opt to discard the result. 
     * @param candidateRecord current record
     * @return CandidateInfo with the position of the record in the ranking and an indication of acceptance.
     */
    CandidateInfo addCandidate(CandidateRecord candidateRecord);

    /**
     * This method retrieves the top N competitors for a given map.
     * @param number Max results
     * @param map Map name
     * @return List of CompetitorInfo with the info of the N better for the map.
     */
    List<CompetitorInfo> getTopCompetitors(int number, String map);

    /**
     * This method retrieves the actual movements for a given position and map.
     * @param number position
     * @param map map
     * @return CandidateRecord with these movements.
     */
    CandidateRecord getGhost(int number, String map);
}
