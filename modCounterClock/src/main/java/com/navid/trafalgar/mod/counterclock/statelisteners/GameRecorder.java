package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.navid.trafalgar.manager.AbortedState;
import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.manager.SuccessfulState;
import com.navid.trafalgar.mod.counterclock.CounterClockGameModel;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.model.StepRecord;
import com.navid.trafalgar.persistence.CandidateInfo;
import com.navid.trafalgar.persistence.localfile.FileRecordPersistenceService;
import com.navid.trafalgar.persistence.recordserver.RecordServerPersistenceService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public final class GameRecorder implements StartedState, PrestartState, SuccessfulState, AbortedState {

    @Autowired
    private GameStatus gameStatus;

    @Autowired
    private GameConfiguration gameConfiguration;

    @Autowired
    private CounterClockGameModel model;

    @Autowired
    private EventManager eventManager;

    private CandidateRecord candidateRecord;

    @Autowired
    private FileRecordPersistenceService fileRecordPersistenceService;

    @Autowired
    private RecordServerPersistenceService recordServerPersistenceService;

    private List<String> eventList = new ArrayList<String>();

    private final int corePoolSize = 4;
    private final int maximumPoolSize = 8;
    private final int keepAliveTime = 5000;

    private final ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            keepAliveTime, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Override
    public void onPrestart(float tpf) {
        eventManager.registerListener(new EventListener() {

            @Override
            public void onEvent(String event) {
                eventList.add(event);
            }
        }, new String[]{"MILESTONE_REACHED"});

        candidateRecord = ((AShipModel) model.getShip()).getCandidateRecordInstance();
        candidateRecord.setMap(gameConfiguration.getMap());
    }

    @Override
    public void onStarted(float tpf) {
        StepRecord newRecord = ((AShipModel) model.getShip()).getSnapshot();
        newRecord.setTimestamp(gameStatus.getTime().getValue());
        newRecord.setEventList(eventList);
        candidateRecord.addStepRecord(newRecord);
        eventList = new ArrayList<String>();
    }

    @Override
    public void onSuccess(float tpf) {
        fileRecordPersistenceService.addCandidate(candidateRecord);
        executorService.submit(new Callable<CandidateInfo>() {
            @Override
            public CandidateInfo call() throws Exception {
                return recordServerPersistenceService.addCandidate(candidateRecord);
            }
        });
    }

    @Override
    public void onAborted(float tpf) {
        candidateRecord = null;
    }

    @Override
    public void onUnload() {

    }

    /**
     * @param gameStatus the gameStatus to set
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    /**
     * @param model the model to set
     */
    public void setModel(CounterClockGameModel model) {
        this.model = model;
    }

    /**
     * @param eventManager the eventManager to set
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * @param fileRecordPersistenceService the fileRecordPersistenceService to
     * set
     */
    public void setFileRecordPersistenceService(FileRecordPersistenceService fileRecordPersistenceService) {
        this.fileRecordPersistenceService = fileRecordPersistenceService;
    }

    /**
     * @param recordServerPersistenceService the recordServerPersistenceService
     * to set
     */
    public void setRecordServerPersistenceService(RecordServerPersistenceService recordServerPersistenceService) {
        this.recordServerPersistenceService = recordServerPersistenceService;
    }

}
