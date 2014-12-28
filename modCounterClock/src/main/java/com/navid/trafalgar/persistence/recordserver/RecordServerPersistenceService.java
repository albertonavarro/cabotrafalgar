package com.navid.trafalgar.persistence.recordserver;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import static com.google.common.collect.Lists.newArrayList;
import com.google.gson.Gson;
import com.navid.lazylogin.context.RequestContextContainer;
import com.navid.recordserver.v2.AddRecordRequest;
import com.navid.recordserver.v2.AddRecordResponse;
import com.navid.recordserver.v2.GetMapRecordsResponse;
import com.navid.recordserver.v2.GetMapRecordsResponse.RankingEntry;
import com.navid.recordserver.v2.GetRecordResponse;
import com.navid.recordserver.v2.V2Resource;
import com.navid.trafalgar.definition2.Entry;
import com.navid.trafalgar.profiles.ProfileManager;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.persistence.CandidateInfo;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public final class RecordServerPersistenceService implements RecordPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordServerPersistenceService.class);

    private final Gson gson = new Gson();

    @Autowired
    private ProfileManager profileManager;

    @Autowired
    private Builder2 builder2;

    @Resource(name = "mod.counterclock.clientRecordServer")
    private V2Resource rankingClient;

    @Resource(name = "mod.counterclock.requestContextContainer")
    private RequestContextContainer container;

    @Override
    public CandidateInfo addCandidate(CandidateRecord candidateRecord) {
        if (!setUpSession()) {
            return null;
        }

        candidateRecord.setMap(candidateRecord.getHeader().getMap().replace("/", "_"));
        AddRecordResponse addRecordResponse = null;
        String sampleReal = gson.toJson(candidateRecord);
        AddRecordRequest addRecordRequest = new AddRecordRequest();
        addRecordRequest.setPayload(sampleReal);
        LOGGER.info("Trying with size " + sampleReal.length());
        addRecordResponse = rankingClient.postRanking(addRecordRequest);

        CandidateInfo returned = new CandidateInfo();
        returned.setAccepted(true);
        returned.setPosition(addRecordResponse.getPosition());
        return returned;
    }

    @Override
    public List<CompetitorInfo> getTopCompetitors(int number, String map, String ship) {
        if (!setUpSession()) {
            return newArrayList();
        }

        String newMapName = map.replace("/", "_");
        GetMapRecordsResponse response = rankingClient.getRankingshipshipmapsmap(newMapName, ship);

        return Lists.transform(response.getRankingEntry(), new Function<RankingEntry, CompetitorInfo>() {
            @Override
            public CompetitorInfo apply(RankingEntry f) {
                CompetitorInfo result = new CompetitorInfo();
                result.setLocal(false);
                result.setPosition(f.getPosition());
                result.setTime(f.getTime());
                result.setUserName(f.getUsername());
                result.setGameId(f.getId());
                return result;
            }
        });
    }

    private boolean setUpSession() {
        if (profileManager.isOnline()) {
            if (container.get().getSessionId() == null) {

                container.get().setSessionId(profileManager.getSessionId());

            }
            return true;
        }

        return false;
    }

    @Override
    public CandidateRecord getGhost(int number, String map, String ship) {
        if (!setUpSession()) {
            return null;
        }

        final CandidateRecord candidate;
        GetRecordResponse response;
        try {
            List<CompetitorInfo> competitorInfos = getTopCompetitors(number, map, ship);
            if (competitorInfos.size() > 0) {
                response = rankingClient.getRankingidid(competitorInfos.get(0).getGameId());
                candidate = gson.fromJson(response.getPayload(), CandidateRecord.class);
            } else {
                return null;
            }

        } catch (Exception e) {
            LOGGER.error("Error loading ghost {} from map {}", ship, map);
            return null;
        }
        
        Entry entry = new Entry();
        entry.setType(candidate.getHeader().getShipModel());
        entry.setValues(new HashMap<String, Object>() {
                    {
                        put("role", "CandidateRecord");
                    }
                });
        Collection cr = builder2.build(entry);

        CandidateRecord finalcandidate = (CandidateRecord) gson.fromJson(response.getPayload(), Iterators.getOnlyElement(cr.iterator()).getClass());

        return finalcandidate;

    }

    /**
     * @param rankingClient the rankingClient to set
     */
    public void setRankingClient(V2Resource rankingClient) {
        this.rankingClient = rankingClient;
    }

    /**
     * @param container the container to set
     */
    public void setContainer(RequestContextContainer container) {
        this.container = container;
    }

    /**
     * @param builder2 the builder2 to set
     */
    public void setBuilder2(Builder2 builder2) {
        this.builder2 = builder2;
    }

    /**
     * @param profileManager the profileManager to set
     */
    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

}
