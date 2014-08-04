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
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.navid.trafalgar.persistence.RecordPersistenceServiceFactory;
import com.navid.trafalgar.persistence.StepRecord;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder implements StartedState, PrestartState, SuccessfulState, AbortedState{
    private GameStatus gameStatus;
    private GameConfiguration gameConfiguration;
   
    private CandidateRecord candidateRecord;
    private RecordPersistenceService persistenceService = RecordPersistenceServiceFactory.getFactory(RecordPersistenceServiceFactory.Type.LOCAL);
   
    private List<String> eventList = new ArrayList<String>();

    public GameRecorder(GameStatus gameStatus, GameConfiguration gameConfiguration, EventManager eventManager) {
        this.gameStatus = gameStatus;
        this.gameConfiguration = gameConfiguration;
       
        eventManager.registerListener(new EventListener() {

            public void onEvent(String event) {
                eventList.add(event);
            }
        }, new String[]{"MILLESTONE_REACHED"});
    }

    @Override
    public void onPrestart(float tpf) {
        //candidateRecord = gameStatus.getMillestoneGameModel().getShip().getCandidateRecordInstance();
        //candidateRecord.setMap(gameConfiguration.getMap());
    }

    @Override
    public void onStarted(float tpf) {
        //StepRecord newRecord = gameStatus.getMillestoneGameModel().getShip().getSnapshot();
        //newRecord.setTimestamp(gameStatus.getTime().getValue());
        //newRecord.setEventList(eventList);
        //candidateRecord.addStepRecord(newRecord);
        //eventList = new ArrayList<String>();
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

}