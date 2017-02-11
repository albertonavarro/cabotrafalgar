package com.navid.trafalgar.mod.counterclock;

import com.navid.trafalgar.mod.common.GameMenuController;
import com.navid.trafalgar.model.GameConfiguration;
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
