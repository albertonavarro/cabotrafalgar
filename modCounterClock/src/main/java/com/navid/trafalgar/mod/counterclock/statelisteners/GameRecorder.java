/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.navid.trafalgar.manager.AbortedState;
import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.manager.SuccessfulState;
import com.navid.trafalgar.mod.counterclock.CounterClockGameModel;
import com.navid.trafalgar.model.AShipModelTwo;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameModel;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.model.ShipModelTwo;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.navid.trafalgar.persistence.RecordPersistenceServiceFactory;
import com.navid.trafalgar.persistence.StepRecord;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public class GameRecorder implements StartedState, PrestartState, SuccessfulState, AbortedState{
    
    @Autowired
    private GameStatus gameStatus;
    
    @Autowired
    private GameConfiguration gameConfiguration;
   
    @Autowired
    private CounterClockGameModel model;
    
    @Autowired
    private EventManager eventManager;
    
    private CandidateRecord candidateRecord;
    
    private RecordPersistenceService persistenceService = RecordPersistenceServiceFactory.getFactory(RecordPersistenceServiceFactory.Type.LOCAL);
   
    private List<String> eventList = new ArrayList<String>();

    public GameRecorder() {
        
    }
    
    @PostConstruct
    public void init () {
        eventManager.registerListener(new EventListener() {

            public void onEvent(String event) {
                eventList.add(event);
            }
        }, new String[]{"MILLESTONE_REACHED"});
    }

    @Override
    public void onPrestart(float tpf) {
        candidateRecord = model.getShip().getCandidateRecordInstance();
        candidateRecord.setMap(gameConfiguration.getMap());
    }

    @Override
    public void onStarted(float tpf) {
        StepRecord newRecord = model.getShip().getSnapshot();
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
    public void onAborted(float tpf){
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
    
    

}