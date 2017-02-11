package com.navid.trafalgar.mod.common;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.maploader.v3.MapDefinition;
import com.navid.trafalgar.model.*;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

/**
 * Created by alberto on 28/12/16.
 */
public class ObjectCreationPhaseController extends GameMenuController {

    @Autowired
    private GameConfiguration gameConfiguration;

    @Autowired
    private ModelBuilder builder;

    @Autowired
    private AssetManager assetManager;

    @Override
    public void doOnStartScreen() {

        MapDefinition gameDefinition = (MapDefinition) assetManager.loadAsset(gameConfiguration.getMap());

        GameModel gameModel = builder.buildControls(gameConfiguration, gameDefinition);

        gameConfiguration.setControls(gameModel);

        gameConfiguration.setGameDefinition(gameDefinition);

        skip();
    }

    @Override
    public void doOnEndScreen() {

    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public void setBuilder(ModelBuilder builder) {
        this.builder = builder;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
}
