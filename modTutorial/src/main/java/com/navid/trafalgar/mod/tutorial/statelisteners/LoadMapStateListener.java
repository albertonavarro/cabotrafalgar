package com.navid.trafalgar.mod.tutorial.statelisteners;

import com.google.common.collect.Lists;
import com.jme3.asset.AssetManager;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.input.SystemInteractions;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.LoadModelState;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.maploader.v3.EntryDefinition;
import com.navid.trafalgar.maploader.v3.MapDefinition;
import com.navid.trafalgar.mod.tutorial.NavigationScreenController;
import com.navid.trafalgar.mod.tutorial.TutorialGameModel;
import com.navid.trafalgar.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

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
    private TutorialGameModel tutorialGameModel;
    @Autowired
    private NavigationScreenController navigationScreenController;

    @Override
    public void onLoadModel(float tpf) {

        MapDefinition gameDefinition = (MapDefinition) assetManager.loadAsset(gameConfiguration.getMap());
        gameStatus.setGameDefinition(gameDefinition);

        gameConfiguration.getCustom().addToModel(newArrayList(navigationScreenController), "system");

        GameModel gameModel = builder2.buildGeometry(gameConfiguration, gameDefinition);

        gameModel.addToModel(Lists.newArrayList(eventManager));
        //gameModel.addToModel(gameConfiguration.getPreGameModel().getByType(SystemInteractions.class));
        gameModel.addToModel(gameConfiguration.getPreGameModel().getByType(AShipModelInteractive.class));
        gameModel.updateDependencies();
        tutorialGameModel.init(gameModel, gameConfiguration.getPreGameModel());

        IContext iContext = tutorialGameModel.getIContext();
        gameStatus.getGameNode().attachChild(iContext.getWind()
        );

        AShipModelPlayer currentShip = tutorialGameModel.getShip();
        gameStatus.getGameNode().addControl((Control) currentShip);
        currentShip.setStatisticsManager(statisticsManager);

        if (tutorialGameModel.getGhost() != null) {
            gameStatus.getGameNode().addControl((Control) tutorialGameModel.getGhost());
        }

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        for (Filter currentFilter : tutorialGameModel.getFpp()) {
            fpp.addFilter(currentFilter);
        }

        gameStatus.getViewPort().addProcessor(fpp);

        gameStatus.getGameNode().attachChild(tutorialGameModel.getGameNode());
    }

    @Override
    public void onUnload() {
        gameStatus.getGameNode().removeControl((Control) tutorialGameModel.getShip());
        if (gameStatus.getGameNode() != null) {
            gameStatus.getGameNode().removeControl((Control) tutorialGameModel.getGhost());
        }

        List<AMilestoneModel> milestones = tutorialGameModel.getMilestones();
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
     * @param tutorialGameModel the tutorialGameModel to set
     */
    public void setTutorialGameModel(TutorialGameModel tutorialGameModel) {
        this.tutorialGameModel = tutorialGameModel;
    }

    public void setNavigationScreenController(NavigationScreenController navigationScreenController) {
        this.navigationScreenController = navigationScreenController;
    }
}
