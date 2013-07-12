package com.navid.trafalgar.mod.windtunnel.statelisteners;

import com.navid.trafalgar.model.AShipModel;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.navid.trafalgar.definition2.GameDefinition2;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.LoadModelState;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.windtunnel.WindTunnelGameModel;
import com.navid.trafalgar.mod.windtunnel.model.AHarnessModel;
import com.navid.trafalgar.model.*;
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
    private WindTunnelGameModel windTunnelGameModel;

    @Override
    public void onLoadModel(float tpf) {

        GameDefinition2 gameDefinition = (GameDefinition2) assetManager.loadAsset("mod/windtunnel/WindTunnel.json2");
        gameStatus.setGameDefinition(gameDefinition);

        GameModel gameModel = builder2.build(gameConfiguration, gameDefinition);

        windTunnelGameModel.init(gameModel);

        IContext iContext = windTunnelGameModel.getIContext();
        gameStatus.getGameNode().attachChild(iContext.getWind().getGeometry());

        AShipModel currentShip = windTunnelGameModel.getShip();
        currentShip.registerInput(inputManager);
        gameStatus.getGameNode().addControl(currentShip);
        AHarnessModel harness = windTunnelGameModel.getHarness();
        gameStatus.getGameNode().addControl(harness);
        
        currentShip.setStatisticsManager(statisticsManager);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        for (Filter currentFilter : windTunnelGameModel.getFpp()) {
            fpp.addFilter(currentFilter);
        }

        gameStatus.getViewPort().addProcessor(fpp);

        gameStatus.getGameNode().attachChild(windTunnelGameModel.getGameNode());
    }

    @Override
    public void onUnload() {
        gameStatus.getGameNode().removeControl(windTunnelGameModel.getShip());
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
    public void setCounterClockGameModel(WindTunnelGameModel windTunnelGameModel) {
        this.windTunnelGameModel = windTunnelGameModel;
    }
}
