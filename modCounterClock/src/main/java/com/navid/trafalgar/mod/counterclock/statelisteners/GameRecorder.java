package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.navid.trafalgar.manager.*;
import com.navid.trafalgar.mod.counterclock.CounterClockGameModel;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.navid.trafalgar.persistence.StepRecord;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class GameRecorder implements StartedState, PrestartState, SuccessfulState, AbortedState {

    @Autowired
    private GameStatus gameStatus;
    @Autowired
    private GameConfiguration gameConfiguration;
    @Autowired
    private RecordPersistenceService persistenceService;
    @Autowired
    private CounterClockGameModel gameModel;
    private CandidateRecord candidateRecord;
    private AShipModel ship;
    private List<String> eventList = new ArrayList<String>();

    @Override
    public void onPrestart(float tpf) {
        ship = gameModel.getShip();
        candidateRecord = ship.getCandidateRecordInstance();
        candidateRecord.setMap(gameConfiguration.getMap());
    }

    @Override
    public void onStarted(float tpf) {
        StepRecord newRecord = ship.getSnapshot();
        newRecord.setTimestamp(gameStatus.getTime().getValue());
        newRecord.setEventList(eventList);
        candidateRecord.addStepRecord(newRecord);
        eventList = new ArrayList<String>();
    }

    @Override
    public void onSuccess(float tpf) {
        persistenceService.addCandidate(candidateRecord);
    }

    @Override
    public void onAborted(float tpf) {
        candidateRecord = null;
    }

    public void onUnload() {
    }

    /**
     * @param gameStatus the gameStatus to set
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * @param gameModel the gameModel to set
     */
    public void setGameModel(CounterClockGameModel gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    @Autowired
    public void setEventManager(EventManager eventManager) {
        eventManager.registerListener(new EventListener() {

            public void onEvent(String event) {
                eventList.add(event);
            }
        }, new String[]{"MILLESTONE_REACHED"});
    }

    /**
     * @param persistenceService the persistenceService to set
     */
    public void setPersistenceService(RecordPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}
