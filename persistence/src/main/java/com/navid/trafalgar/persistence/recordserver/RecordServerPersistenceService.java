package com.navid.trafalgar.persistence.recordserver;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.navid.lazylogin.CreateTokenRequest;
import com.navid.lazylogin.CreateTokenResponse;
import com.navid.lazylogin.UserCommands;
import com.navid.lazylogin.context.RequestContextContainer;
import com.navid.recordserver.v1.AddRecordRequest;
import com.navid.recordserver.v1.AddRecordResponse;
import com.navid.recordserver.v1.GetMapRecordsResponse;
import com.navid.recordserver.v1.GetMapRecordsResponse.RankingEntry;
import com.navid.recordserver.v1.RankingResource;
import com.navid.trafalgar.persistence.CandidateInfo;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author anf
 */
public class RecordServerPersistenceService implements RecordPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordServerPersistenceService.class);

    private Gson gson = new Gson();

    @Resource(name = "mod.counterclock.clientUser")
    private UserCommands userCommandsClient;

    @Resource(name = "mod.counterclock.clientRecordServer")
    private RankingResource rankingClient;

    @Resource(name = "mod.counterclock.requestContextContainer")
    private RequestContextContainer container;

    @Override
    public CandidateInfo addCandidate(CandidateRecord candidateRecord) {
        setUpSession();
        
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
        setUpSession();
                 
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
                return result;
            }
        });
    }

    private void setUpSession() {
        if (container.get().getSessionId() == null) {
            CreateTokenRequest ctr = new CreateTokenRequest();
            ctr.setEmail("fakeclient@email.com");
            CreateTokenResponse response = userCommandsClient.createToken(ctr);

            container.get().setSessionId(response.getSessionid().getSessionid());
        }
    }

    @Override
    public CandidateRecord getGhost(int number, String map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @param userCommandsClient the userCommandsClient to set
     */
    public void setUserCommandsClient(UserCommands userCommandsClient) {
        this.userCommandsClient = userCommandsClient;
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

    /*private String generatePayload(int index) {
     StringBuilder sb = new StringBuilder();
     sb.append("{\"version\":1,\"header\":{\"map\":\"" + "map" + "\",\"shipModel\":\"ShipModelOneX\"},\"stepRecordList\":[");
     sb.append("{\"position\":{\"x\":0.0,\"y\":0.0,\"z\":0.0},\"rotation\":{\"x\":0.0,\"y\":0.0,\"z\":0.0,\"w\":1.0},\"timestamp\":0.13044842,\"eventList\":[]},");

     for (int indexindex = 0; indexindex < index; indexindex++) {
     for(int index3 = 0; index3 < 10000; index3++){
     sb.append(",{\"position\":{\"x\":0.0,\"y\":0.0,\"z\":0.0},\"rotation\":{\"x\":0.0,\"y\":0.0,\"z\":0.0,\"w\":1.0},\"timestamp\":0.13044842,\"eventList\":[]}");
     }
     }
     sb.append("]}");
     return sb.toString();
     }*/
}
