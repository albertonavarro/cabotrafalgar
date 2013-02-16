package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.navid.trafalgar.definition2.Entry;
import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.navid.trafalgar.persistence.StepRecord;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author anf
 */
public class GhostViewer implements PrestartState, StartedState {

    private AShipModel ghost;
    private CandidateRecord ghostRecord;
    private boolean ghostAvailable;
    private int ghostIndex;
    
    @Autowired
    private GameStatus gameStatus;
    
    @Autowired
    private GameConfiguration gameConfiguration;

    @Autowired
    private RecordPersistenceService persistenceService;
    
    @Autowired
    private Builder2 builder;

    public void onPrestart(float tpf) {
        ghostIndex = 0;

        if (gameConfiguration.isShowGhost()) {
            ghostRecord = persistenceService.getGhost(1, gameConfiguration.getMap());
            if (ghostRecord != null) {
                Entry entry = new Entry(){{
                    setName("ghost");
                    setType(ghostRecord.getHeader().getShipModel());
                }};
                ghost = (AShipModel) builder.build(entry);
                ghost.setTransparent(true);
            } 
        }

        if (ghost != null && ghostRecord.getStepRecord() != null) {
            ghostAvailable = true;
            ((Node) gameStatus.getGameNode().getChild("reflexion")).attachChild(ghost);
        }
    }

    public void onStarted(float tpf) {
        if (ghostAvailable) {
            while (ghostIndex < ghostRecord.getStepRecord().size()) {
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

    /**
     * @param persistence the persistence to set
     */
    public void setPersistence(RecordPersistenceService persistence) {
        this.persistenceService = persistence;
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
     * @param builder the builder to set
     */
    public void setBuilder(Builder2 builder) {
        this.builder = builder;
    }
}
