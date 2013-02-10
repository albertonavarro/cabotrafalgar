package com.navid.trafalgar.mod.windtunnel.statelisteners;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.LoadModelState;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.model.mod.counterclock.AMillestoneModel;
import java.util.List;

/**
 *
 * @author anf
 */
public class LoadMapStateListener implements LoadModelState {

    private AssetManager assetManager;
    private InputManager inputManager;
    private GameStatus gameStatus;
    private GameConfiguration gameConfiguration;
    private EventManager eventManager;
    private StatisticsManager statisticsManager;

    public LoadMapStateListener(Application app, EventManager eventManager, GameStatus gameStatus, GameConfiguration gameConfiguration, StatisticsManager statisticsManager) {
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        this.gameConfiguration = gameConfiguration;
        this.gameStatus = gameStatus;
        this.eventManager = eventManager;
        this.statisticsManager = statisticsManager;
    }

    public void onLoadModel(float tpf) {

        /*GameDefinition gameDefinition = (GameDefinition) assetManager.loadAsset(gameConfiguration.getMap());
        //gameStatus.setGameDefinition(gameDefinition);

        GameModel gameModel = gameConfiguration.getModelBuilder().buildGameModel(gameDefinition, gameConfiguration);
        gameStatus.setGameModel(gameModel);

        IContext iContext = gameModel.getiContext();
        gameStatus.getGameNode().attachChild(iContext.getWind().getGeometry());

        Map<String, AShipModel> ships = gameModel.getShips();
        for (AShipModel currentShip : ships.values()) {
            currentShip.registerInput(inputManager);
            gameStatus.getGameNode().addControl(currentShip);
            currentShip.setStatisticsManager(statisticsManager);
        }

        if(gameModel.getHarness() != null){
            gameStatus.getGameNode().addControl(gameModel.getHarness());
            gameModel.getHarness().registerInputManager(inputManager);
        }

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        for (Filter currentFilter : gameModel.getFpp()) {
            fpp.addFilter(currentFilter);
        }

        gameStatus.getViewPort().addProcessor(fpp);
        gameStatus.getGameNode().attachChild(gameModel.getGameNode());*/
    }

    public void onUnload() {
        
        gameStatus.getGameNode().removeControl(gameStatus.getMillestoneGameModel().getShip());

        List<AMillestoneModel> millestones = gameStatus.getMillestoneGameModel().getMillestones();
        for (AMillestoneModel currentMillestone : millestones) {
            gameStatus.getGameNode().removeControl(currentMillestone);
        }
        gameStatus.getGameNode().detachAllChildren();
        gameStatus.setGameDefinition(null);
        gameStatus.setGameModel(null);

    }
}
