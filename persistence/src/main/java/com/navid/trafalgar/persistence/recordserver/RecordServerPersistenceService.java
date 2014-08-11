package com.navid.trafalgar.persistence.recordserver;

import com.google.gson.Gson;
import com.navid.lazylogin.CreateTokenRequest;
import com.navid.lazylogin.CreateTokenResponse;
import com.navid.lazylogin.UserCommands;
import com.navid.lazylogin.context.RequestContextContainer;
import com.navid.recordserver.v1.AddRecordRequest;
import com.navid.recordserver.v1.AddRecordResponse;
import com.navid.recordserver.v1.RankingResource;
import com.navid.trafalgar.persistence.CandidateInfo;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;

/**
 *
 * @author anf
 */
public class RecordServerPersistenceService implements RecordPersistenceService {

    private Gson gson = new Gson();

    @Resource(name = "mod.counterclock.clientUser")
    private UserCommands userCommandsClient;

    @Resource(name = "mod.counterclock.clientRecordServer")
    private RankingResource rankingClient;

    @Resource(name = "mod.counterclock.requestContextContainer")
    private RequestContextContainer container;

    @Override
    public CandidateInfo addCandidate(CandidateRecord candidateRecord) {
        container.create();

        CreateTokenRequest ctr = new CreateTokenRequest();
        ctr.setEmail("fakeclient@email.com");
        CreateTokenResponse response = userCommandsClient.createToken(ctr);

        container.get().setRequestId(response.getSessionid().getSessionid());

        AddRecordRequest addRecordRequest = new AddRecordRequest();
        addRecordRequest.setPayload(getPayload("map1", "62.0"));
        AddRecordResponse addRecordResponse = rankingClient.post(addRecordRequest);

        CandidateInfo returned = new CandidateInfo();
        returned.setAccepted(true);
        returned.setPosition(addRecordResponse.getPosition());
        return returned;
    }
    
    private String getPayload(String map, String finalTime) {
        return "{\"version\":1,\"header\":{\"map\":\"" + map + "\",\"shipModel\":\"ShipModelOneX\"},\"stepRecordList\":[{\"position\":{\"x\":0.0,\"y\":0.0,\"z\":0.0},\"rotation\":{\"x\":0.0,\"y\":0.0,\"z\":0.0,\"w\":1.0},\"timestamp\":0.13044842,\"eventList\":[]},{\"position\":{\"x\":-267.15237,\"y\":0.0,\"z\":-784.9582},\"rotation\":{\"x\":-0.08990571,\"y\":-0.89595354,\"z\":0.21214685,\"w\":-0.3796915},\"timestamp\":"+finalTime+",\"eventList\":[\"MILLESTONE_REACHED\"]}]}";
    }

    @Override
    public List<CompetitorInfo> getTopCompetitors(int number, String map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
