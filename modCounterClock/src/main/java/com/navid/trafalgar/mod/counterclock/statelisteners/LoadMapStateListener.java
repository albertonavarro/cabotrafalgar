package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.navid.trafalgar.definition2.GameDefinition2;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.LoadModelState;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.counterclock.CounterClockGameModel;
import com.navid.trafalgar.mod.counterclock.model.AMillestoneModel;
import com.navid.trafalgar.model.*;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author anf
 */
public class LoadMapStateListener implements LoadModelState {

    @Autowired
    private AssetManager assetManager;
    @Autowired
    private InputManager inputManager;
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

        counterClockGameModel.init(gameModel);

        IContext iContext = counterClockGameModel.getIContext();
        gameStatus.getGameNode().attachChild(iContext.getWind().getGeometry());

        AShipModel currentShip = counterClockGameModel.getShip();
        currentShip.registerInput(inputManager);
        gameStatus.getGameNode().addControl(currentShip);
        currentShip.setStatisticsManager(statisticsManager);

        List<AMillestoneModel> millestones = counterClockGameModel.getMillestones();
        for (AMillestoneModel currentMillestone : millestones) {
            currentMillestone.setEventManager(eventManager);
            currentMillestone.setCollidable(Collections.singleton(currentShip));
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
        gameStatus.getGameNode().removeControl(counterClockGameModel.getShip());

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
     * @param inputManager the inputManager to set
     */
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
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
