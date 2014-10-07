package com.navid.trafalgar.persistence.recordserver;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import static com.google.common.collect.Lists.newArrayList;
import com.google.gson.Gson;
import com.lazylogin.client.user.v0.CreateTokenRequest;
import com.lazylogin.client.user.v0.CreateTokenResponse;
import com.lazylogin.client.user.v0.UserCommands;
import com.navid.lazylogin.context.RequestContextContainer;
import com.navid.recordserver.v1.AddRecordRequest;
import com.navid.recordserver.v1.AddRecordResponse;
import com.navid.recordserver.v1.GetMapRecordsResponse;
import com.navid.recordserver.v1.GetMapRecordsResponse.RankingEntry;
import com.navid.recordserver.v1.GetRecordResponse;
import com.navid.recordserver.v1.RankingResource;
import com.navid.trafalgar.definition2.Entry;
import com.navid.trafalgar.mod.counterclock.profile.ProfileManager;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.persistence.CandidateInfo;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.navid.trafalgar.persistence.localfile.FileRecordPersistenceService;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author anf
 */
public class RecordServerPersistenceService implements RecordPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordServerPersistenceService.class);

    private Gson gson = new Gson();

    @Autowired
    private ProfileManager profileManager;

    @Autowired
    private Builder2 builder2;

    @Resource(name = "mod.counterclock.clientRecordServer")
    private RankingResource rankingClient;

    @Resource(name = "mod.counterclock.requestContextContainer")
    private RequestContextContainer container;

    @Override
    public CandidateInfo addCandidate(CandidateRecord candidateRecord) {
        if(!setUpSession()){
            return null;
        }

        candidateRecord.setMap(candidateRecord.getHeader().getMap().replace("/", "_"));
        AddRecordResponse addRecordResponse = null;
        String sampleReal = gson.toJson(candidateRecord);
        AddRecordRequest addRecordRequest = new AddRecordRequest();
        addRecordRequest.setPayload(sampleReal);
        LOGGER.info("Trying with size " + sampleReal.length());
        addRecordResponse = rankingClient.post(addRecordRequest);

        CandidateInfo returned = new CandidateInfo();
        returned.setAccepted(true);
        returned.setPosition(addRecordResponse.getPosition());
        return returned;
    }

    @Override
    public List<CompetitorInfo> getTopCompetitors(int number, String map) {
        if (!setUpSession()){
            return newArrayList();
        }

        String newMapName = map.replace("/", "_");
        GetMapRecordsResponse response = rankingClient.getMapsmap(newMapName);

        return Lists.transform(response.getRankingEntry(), new Function<RankingEntry, CompetitorInfo>() {
            @Override
            public CompetitorInfo apply(RankingEntry f) {
                CompetitorInfo result = new CompetitorInfo();
                result.setLocal(false);
                result.setPosition(f.getPosition());
                result.setTime(f.getTime());
                result.setUserName(f.getId());
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
    public CandidateRecord getGhost(int number, String map) {
        if(! setUpSession()){
            return null;
        }

        GetRecordResponse response = rankingClient.getIdid(getTopCompetitors(number, map).get(0).getGameId());
        final CandidateRecord candidate;
        try {
            candidate = gson.fromJson(response.getPayload(), CandidateRecord.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Collection cr = builder2.build(new Entry() {
            {
                setType(candidate.getHeader().getShipModel());
                setValues(new HashMap<String, Object>() {
                    {
                        put("role", "CandidateRecord");
                    }
                });
            }
        });

        CandidateRecord finalcandidate = (CandidateRecord) gson.fromJson(response.getPayload(), Iterators.getOnlyElement(cr.iterator()).getClass());

        return finalcandidate;

    }

    /**
     * @param rankingClient the rankingClient to set
     */
    public void setRankingClient(RankingResource rankingClient) {
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
