package com.navid.trafalgar.mod.windtunnel;

import com.navid.trafalgar.maploader.v3.MapDefinition;
import com.navid.trafalgar.mod.common.GameMenuController;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameModel;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.model.ModelBuilder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alberto on 28/12/16.
 */
public class ModConfigPhaseController extends GameMenuController {

    @Autowired
    private GameConfiguration gameConfiguration;

    @Override
    public void doOnStartScreen() {
        gameConfiguration.setMap("Maps/WindTunnel/WindTunnel.map");

        skip();
    }

    @Override
    public void doOnEndScreen() {

    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }
}
