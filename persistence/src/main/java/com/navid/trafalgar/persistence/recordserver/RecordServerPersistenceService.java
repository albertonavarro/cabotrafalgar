
package com.navid.trafalgar.persistence.recordserver;

import com.navid.trafalgar.persistence.CandidateInfo;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import java.util.List;

/**
 *
 * @author anf
 */
public class RecordServerPersistenceService implements RecordPersistenceService {

    @Override
    public CandidateInfo addCandidate(CandidateRecord candidateRecord) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CompetitorInfo> getTopCompetitors(int number, String map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CandidateRecord getGhost(int number, String map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
