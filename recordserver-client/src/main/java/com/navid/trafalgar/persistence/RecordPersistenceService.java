package com.navid.trafalgar.persistence;

import com.navid.codegen.recordserver.ApiException;
import com.navid.trafalgar.events.ServerStatusEvent;
import com.navid.trafalgar.model.CandidateRecord;
import java.util.List;

/**
 *
 * @author alberto
 */
public interface RecordPersistenceService {

    enum Status {
        UP, BUSY, DOWN, UNKNOWN
    }

    Status getStatus();

    /**
     * This method asks for recording the current result. Recording is not
     * guaranteed as system could opt to discard the result.
     *
     * @param candidateRecord current record
     * @return CandidateInfo with the position of the record in the ranking and
     * an indication of acceptance.
     */
    CandidateInfo addCandidate(CandidateRecord candidateRecord, String sessionId);

    /**
     * This method retrieves the top N competitors for a given map.
     *
     * @param number Max results
     * @param map Map name
     * @param ship
     * @return List of CompetitorInfo with the info of the N better for the map.
     */
    List<CompetitorInfo> getTopCompetitors(int number, String map, String ship, String sessionId) throws ApiException;

    /**
     * This method retrieves the actual movements for a given position and map.
     *
     * @param number position
     * @param map map
     * @param ship
     * @return CandidateRecord with these movements.
     */
    CandidateRecord getGhost(int number, String map, String ship, String sessionId) throws ApiException;

}
