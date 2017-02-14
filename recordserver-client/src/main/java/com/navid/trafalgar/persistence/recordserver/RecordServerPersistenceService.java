package com.navid.trafalgar.persistence.recordserver;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.navid.codegen.recordserver.ApiClient;
import com.navid.codegen.recordserver.ApiException;
import com.navid.codegen.recordserver.api.DefaultApi;
import com.navid.codegen.recordserver.model.*;
import com.navid.lazylogin.context.RequestContextContainer;
import com.navid.trafalgar.maploader.v3.EntryDefinition;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.model.ModelBuilder;
import com.navid.trafalgar.model.Role;
import com.navid.trafalgar.persistence.CandidateInfo;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.navid.trafalgar.persistence.RecordServerStatusChange;
import com.navid.trafalgar.profiles.ProfileManager;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.bushe.swing.event.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public final class RecordServerPersistenceService implements RecordPersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(RecordServerPersistenceService.class);

    private final Gson gson = new Gson();

    @Autowired
    private ProfileManager profileManager;

    @Autowired
    private ModelBuilder builder2;

    //manually wired
    private DefaultApi defaultApi;

    @Autowired
    private RequestContextContainer requestContextContainer;

    @Resource(name = "mod.counterclock.requestContextContainer")
    private RequestContextContainer container;

    @Autowired
    private TaskScheduler executor;

    @Autowired
    private EventService eventService;

    private volatile Status currentStatus = Status.UNKNOWN;

    public RecordServerPersistenceService(String recordserverUrl) {
        defaultApi = new DefaultApi();
        ApiClient apiClient = defaultApi.getApiClient();
        apiClient.setBasePath(recordserverUrl);
    }

    public void init(){
        executor.scheduleAtFixedRate(new StatusChecker(), 3000);
    }

    private class StatusChecker implements Runnable {

        @Override
        public void run() {
            ResponseBody body = null;
            try {
                Call call = defaultApi.getApiClient().buildCall("/health", "GET", newArrayList(), null, new HashMap<>(), null, new String[0], null);
                final Response resp = call.execute();
                final int code = resp.code();
                currentStatus = code == 200? Status.OK : Status.DOWN;
                body = resp.body();
            } catch (Exception e) {
                currentStatus = Status.DOWN;
            } finally {
                if (body != null) {
                    IOUtils.closeQuietly(body);
                }
            }

            eventService.publish(new RecordServerStatusChange(currentStatus));
        }
    }

    @Override
    public Status getStatus() {
        return currentStatus;
    }

    @Override
    public CandidateInfo addCandidate(CandidateRecord candidateRecord, String sessionId) {
        if (!setUpSession()) {
            CandidateInfo returned = new CandidateInfo();
            returned.setAccepted(false);
            return returned;
        }

        candidateRecord.setMap(sanitiseMapName(candidateRecord.getHeader().getMap()));
        String sampleReal = gson.toJson(candidateRecord);
        NewMapEntryRequest newMapEntryRequest = new NewMapEntryRequest().payload(sampleReal);

        LOG.info("Trying with size " + sampleReal.length());
        try {
            NewMapEntryResponse response = defaultApi.postRanking(newMapEntryRequest, sessionId, null);
            CandidateInfo returned = new CandidateInfo();
            returned.setAccepted(true);
            returned.setPosition(response.getPosition());
            return returned;
        } catch (Exception e) {
            CandidateInfo returned = new CandidateInfo();
            returned.setAccepted(false);
            return returned;
        }
    }

    @Override
    public List<CompetitorInfo> getTopCompetitors(int number, String map, String ship, String sessionId) throws ApiException {
        if (!setUpSession()) {
            return newArrayList();
        }

        RankingEntry response;

        response  = defaultApi.getByShipAndMap(sanitiseMapName(map), ship, sessionId, null);
        return Lists.transform(response.getRankingEntry(), f -> {
            CompetitorInfo result = new CompetitorInfo();
            result.setLocal(false);
            result.setPosition(f.getPosition());
            result.setTime(f.getTime());
            result.setUserName(f.getUsername());
            result.setGameId(f.getId());
            return result;
        });
    }

    private boolean setUpSession() {
        if (profileManager.isOnline() && (currentStatus != Status.BUSY || currentStatus != Status.DOWN)) {
            if (container.get().getSessionId() == null) {
                container.get().setSessionId(profileManager.getSessionId());
            }
            return container.get().getSessionId() != null;
        }

        return false;
    }

    @Override
    public CandidateRecord getGhost(int number, String map, String ship, String sessionId) throws ApiException {
        if (!setUpSession()) {
            return null;
        }

        final CandidateRecord candidate;
        MapEntry response;

        List<CompetitorInfo> competitorInfos = getTopCompetitors(number, map, ship, sessionId);
        if (competitorInfos.size() > 0) {
            response = defaultApi.getById(competitorInfos.get(0).getGameId(), sessionId, null);
            candidate = gson.fromJson(response.getPayload(), CandidateRecord.class);
        } else {
            return null;
        }

        EntryDefinition entry = new EntryDefinition();
        entry.setType(candidate.getHeader().getShipModel());
        entry.setValues(new HashMap<String, Object>() {
            {
                put("role", "CandidateRecord");
            }
        });
        Collection cr = builder2.build(entry, Role.candidateRecord);

        CandidateRecord finalcandidate
                = (CandidateRecord) gson.fromJson(response.getPayload(), Iterators.getOnlyElement(cr.iterator()).getClass());

        return finalcandidate;

    }

    private String sanitiseMapName(String mapName) {
        return mapName.replace('/', '_').replace('.', '_');
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
    public void setModelBuilder(ModelBuilder builder2) {
        this.builder2 = builder2;
    }

    /**
     * @param profileManager the profileManager to set
     */
    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public void setRequestContextContainer(RequestContextContainer requestContextContainer) {
        this.requestContextContainer = requestContextContainer;
    }

    public void setExecutor(TaskScheduler executor) {
        this.executor = executor;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

}
