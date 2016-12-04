package com.navid.trafalgar.persistence.recordserver;

import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.persistence.CandidateInfo;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by alberto on 02/12/16.
 */
public class RecordServerPersistenceServiceHystrixProxy implements RecordPersistenceService {

    private int TIMEOUT = 3000;

    private static final Logger LOG = LoggerFactory.getLogger(RecordServerPersistenceService.class);

    private final RecordPersistenceService recordPersistenceService;

    public RecordServerPersistenceServiceHystrixProxy(RecordPersistenceService recordPersistenceService) {
        this.recordPersistenceService = recordPersistenceService;
    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public CandidateInfo addCandidate(CandidateRecord candidateRecord, String sessionId) {
        return new HystrixCommand<CandidateInfo>(HystrixCommandGroupKey.Factory.asKey("RecordServer"), TIMEOUT) {
            @Override
            public final CandidateInfo run() throws Exception {
                return recordPersistenceService.addCandidate(candidateRecord, sessionId);
            }

            @Override
            public final CandidateInfo getFallback() {
                CandidateInfo returned = new CandidateInfo();
                returned.setAccepted(false);
                returned.setReasonCode("Server not reachable at this moment.");
                return returned;
            }
        }.execute();
    }

    @Override
    public List<CompetitorInfo> getTopCompetitors(int number, String map, String ship, String sessionId) {
        return new HystrixCommand<List<CompetitorInfo>>(HystrixCommandGroupKey.Factory.asKey("RecordServer"), TIMEOUT) {
            @Override
            public final List<CompetitorInfo> run() throws Exception {
                return recordPersistenceService.getTopCompetitors(number, map, ship, sessionId);
            }

            @Override
            public final List<CompetitorInfo> getFallback() {
                LOG.info("Connectivity problem retrieving top competitors, returning empty");
                return newArrayList();
            }
        }.execute();
    }

    @Override
    public CandidateRecord getGhost(int number, String map, String ship, String sessionId) {
        return new HystrixCommand<CandidateRecord>(HystrixCommandGroupKey.Factory.asKey("RecordServer"), 10000) {
            @Override
            public final CandidateRecord run() throws Exception {
                return recordPersistenceService.getGhost(number, map, ship, sessionId);
            }

            @Override
            public final CandidateRecord getFallback() {
                LOG.error("Error loading ghost {} from map {}", ship, map);
                return null;
            }
        }.execute();
    }
}
