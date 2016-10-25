package com.navid.trafalgar.mod.windtunnel.statelisteners;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.maploader.v3.MapDefinition;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.LoadModelState;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.windtunnel.WindTunnelGameModel;
import com.navid.trafalgar.mod.windtunnel.WindTunnelMainScreen;
import com.navid.trafalgar.mod.windtunnel.model.AHarnessModel;
import com.navid.trafalgar.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.collect.Lists.newArrayList;

public final class LoadMapStateListener implements LoadModelState {

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
    private ModelBuilder builder2;
    @Autowired
    private WindTunnelGameModel windTunnelGameModel;
    @Autowired
    private WindTunnelMainScreen windTunnelMainScreen;

    @Override
    public void onLoadModel(float tpf) {

        MapDefinition gameDefinition = (MapDefinition) assetManager.loadAsset("Maps/WindTunnel/WindTunnel.map");
        gameStatus.setGameDefinition(gameDefinition);

        GameModel gameModel = builder2.build(gameConfiguration, gameDefinition);

        gameConfiguration.getPreGameModel().addToModel(newArrayList(windTunnelMainScreen), "system");

        windTunnelGameModel.init(gameModel, gameConfiguration.getPreGameModel());

        IContext iContext = windTunnelGameModel.getIContext();
        gameStatus.getGameNode().attachChild(iContext.getWind().getGeometry());

        AShipModelPlayer currentShip = windTunnelGameModel.getShip();
        gameStatus.getGameNode().addControl((Control) currentShip);
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
        gameStatus.getGameNode().removeControl((Control) windTunnelGameModel.getShip());
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
    public void setModelBuilder(ModelBuilder builder2) {
        this.builder2 = builder2;
    }

    /**
     * @param windTunnelGameModel the windTunnelGameModel to set
     */
    public void setCounterClockGameModel(WindTunnelGameModel windTunnelGameModel) {
        this.windTunnelGameModel = windTunnelGameModel;
    }

    public void setWindTunnelMainScreen(WindTunnelMainScreen windTunnelMainScreen) {
        this.windTunnelMainScreen = windTunnelMainScreen;
    }
}
