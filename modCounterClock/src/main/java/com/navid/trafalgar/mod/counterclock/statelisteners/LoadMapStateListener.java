package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.jme3.asset.AssetManager;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.maploader.v3.EntryDefinition;
import com.navid.trafalgar.maploader.v3.MapDefinition;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.LoadModelState;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.counterclock.CounterClockGameModel;
import com.navid.trafalgar.mod.counterclock.model.AMilestoneModel;
import com.navid.trafalgar.model.*;
import com.navid.trafalgar.model.AShipModel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public final class LoadMapStateListener implements LoadModelState {

    @Autowired
    private AssetManager assetManager;
    @Autowired
    private GameStatus gameStatus;
    @Autowired
    private GameConfiguration gameConfiguration;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private StatisticsManager statisticsManager;
    @Autowired
    private ModelBuilder builder2;
    @Autowired
    private CounterClockGameModel counterClockGameModel;

    @Override
    public void onLoadModel(float tpf) {

        MapDefinition gameDefinition = (MapDefinition) assetManager.loadAsset(gameConfiguration.getMap());
        gameStatus.setGameDefinition(gameDefinition);

        GameModel gameModel = builder2.build(gameConfiguration, gameDefinition);

        if (gameConfiguration.getPreGameModel().contains(CandidateRecord.class)) {
            final CandidateRecord cr = gameConfiguration.getPreGameModel().getSingleByType(CandidateRecord.class);
            EntryDefinition entry = new EntryDefinition();
            entry.setType(gameConfiguration.getShipName());
            entry.setName("ghost1");
            entry.setValues(new HashMap<String, Object>() {
                {
                    put("role", "Ghost");
                    put("record", cr);
                }
            });
            Collection c = builder2.buildWithDependencies(entry, gameModel);

            gameModel.addToModel(c);
        }

        counterClockGameModel.init(gameModel, gameConfiguration.getPreGameModel());

        IContext iContext = counterClockGameModel.getIContext();
        gameStatus.getGameNode().attachChild(iContext.getWind().getGeometry());

        AShipModelPlayer currentShip = counterClockGameModel.getShip();
        gameStatus.getGameNode().addControl((Control) currentShip);
        currentShip.setStatisticsManager(statisticsManager);

        if (counterClockGameModel.getGhost() != null) {
            gameStatus.getGameNode().addControl((Control) counterClockGameModel.getGhost());
        }

        List<AMilestoneModel> milestones = counterClockGameModel.getMilestones();
        for (AMilestoneModel currentMilestone : milestones) {
            currentMilestone.setEventManager(eventManager);
            currentMilestone.setCollidable(Collections.singleton((AShipModel) currentShip));
            gameStatus.getGameNode().addControl(currentMilestone);
        }

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        for (Filter currentFilter : counterClockGameModel.getFpp()) {
            fpp.addFilter(currentFilter);
        }

        gameStatus.getViewPort().addProcessor(fpp);

        gameStatus.getGameNode().attachChild(counterClockGameModel.getGameNode());
    }

    @Override
    public void onUnload() {
        gameStatus.getGameNode().removeControl((Control) counterClockGameModel.getShip());
        if (gameStatus.getGameNode() != null) {
            gameStatus.getGameNode().removeControl((Control) counterClockGameModel.getGhost());
        }

        List<AMilestoneModel> milestones = counterClockGameModel.getMilestones();
        for (AMilestoneModel currentMilestone : milestones) {
            gameStatus.getGameNode().removeControl(currentMilestone);
        }

        gameStatus.getGameNode().detachAllChildren();
        gameStatus.setGameDefinition(null);
    }

    /**
     * @param assetManager the assetManager to set
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
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
     * @param eventManager the eventManager to set
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * @param statisticsManager the statisticsManager to set
     */
    public void setStatisticsManager(StatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    /**
     * @param builder2 the builder2 to set
     */
    public void setModelBuilder(ModelBuilder builder2) {
        this.builder2 = builder2;
    }

    /**
     * @param counterClockGameModel the counterClockGameModel to set
     */
    public void setCounterClockGameModel(CounterClockGameModel counterClockGameModel) {
        this.counterClockGameModel = counterClockGameModel;
    }
}
