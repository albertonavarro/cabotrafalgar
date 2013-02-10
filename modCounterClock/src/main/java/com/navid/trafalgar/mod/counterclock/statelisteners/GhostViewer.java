package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.navid.trafalgar.persistence.RecordPersistenceServiceFactory;
import com.navid.trafalgar.persistence.StepRecord;

/**
 *
 * @author anf
 */
public class GhostViewer implements PrestartState, StartedState {

    private AShipModel ghost;
    private CandidateRecord ghostRecord;
    private boolean ghostAvailable;
    private int ghostIndex;
    private RecordPersistenceService persistenceService =
            RecordPersistenceServiceFactory.getFactory(RecordPersistenceServiceFactory.Type.LOCAL);
    private GameStatus gameStatus;
    private GameConfiguration gameConfiguration;

    public GhostViewer(GameStatus gameStatus, GameConfiguration gameConfiguration) {
        this.gameStatus = gameStatus;
        this.gameConfiguration = gameConfiguration;
    }

    public void onPrestart(float tpf) {
        ghostIndex = 0;

        if (gameConfiguration.isShowGhost()) {
            ghostRecord = persistenceService.getGhost(1, gameConfiguration.getMap());
            if (ghostRecord != null) {
                //ghost = gameConfiguration.getModelBuilder().buildGhost(gameStatus.getGameDefinition(), ghostRecord.getHeader().getShipModel());
            }

        }

        if (ghost != null && ghostRecord.getStepRecord() != null) {
            ghostAvailable = true;
            gameStatus.getGameNode().attachChild(ghost);
        }
    }

    public void onStarted(float tpf) {
        if (ghostAvailable) {
            for (; ghostIndex < ghostRecord.getStepRecord().size();) {
                StepRecord currentStepRecord = (StepRecord) ghostRecord.getStepRecord().get(ghostIndex);
                if (currentStepRecord.getTimestamp() < gameStatus.getTime().getValue()) {
                    ghost.updateFromRecord(currentStepRecord);
                    ghostIndex++;
                } else {
                    break;
                }
            }
        }
    }

    public void onUnload() {
        if (ghost != null) {
            gameStatus.getGameNode().detachChild(ghost);
        }
    }
}
