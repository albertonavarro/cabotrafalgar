package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.jme3.asset.AssetManager;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.definition2.Entry;
import com.navid.trafalgar.definition2.GameDefinition2;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.LoadModelState;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.counterclock.CounterClockGameModel;
import com.navid.trafalgar.mod.counterclock.model.AMillestoneModel;
import com.navid.trafalgar.model.*;
import com.navid.trafalgar.model.AShipModel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class LoadMapStateListener implements LoadModelState {

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
    private Builder2 builder2;
    @Autowired
    private CounterClockGameModel counterClockGameModel;

    @Override
    public void onLoadModel(float tpf) {

        GameDefinition2 gameDefinition = (GameDefinition2) assetManager.loadAsset(gameConfiguration.getMap());
        gameStatus.setGameDefinition(gameDefinition);

        GameModel gameModel = builder2.build(gameConfiguration, gameDefinition);

        if (gameConfiguration.getPreGameModel().contains(CandidateRecord.class)) {
            final CandidateRecord cr = gameConfiguration.getPreGameModel().getSingleByType(CandidateRecord.class);
            Entry entry = new Entry();
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

        List<AMillestoneModel> millestones = counterClockGameModel.getMillestones();
        for (AMillestoneModel currentMillestone : millestones) {
            currentMillestone.setEventManager(eventManager);
            currentMillestone.setCollidable(Collections.singleton((AShipModel) currentShip));
            gameStatus.getGameNode().addControl(currentMillestone);
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

        List<AMillestoneModel> millestones = counterClockGameModel.getMillestones();
        for (AMillestoneModel currentMillestone : millestones) {
            gameStatus.getGameNode().removeControl(currentMillestone);
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
    public void setBuilder2(Builder2 builder2) {
        this.builder2 = builder2;
    }

    /**
     * @param counterClockGameModel the counterClockGameModel to set
     */
    public void setCounterClockGameModel(CounterClockGameModel counterClockGameModel) {
        this.counterClockGameModel = counterClockGameModel;
    }
}
