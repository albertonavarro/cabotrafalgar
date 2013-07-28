/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.mod.windtunnel;

import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class SelectKeyboardControlsScreenController implements ScreenController {
    
    @Autowired
    private ScreenFlowManager screenFlowManager;

    /**
     * From bind
     */
    private Nifty nifty;
    /**
     * From bind
     */
    private Screen screen;
    
    /**
     * Singleton
     */
    @Autowired
    private GameConfiguration gameConfiguration;
    
    /**
     *
     */
    @Autowired
    private GeneratorBuilder generatorBuilder;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {

    }

    @Override
    public void onEndScreen() {

    }
    
    public void goTo(String nextScreen) {
        nifty.gotoScreen(nextScreen);
    }
    
    public void next(){
        screenFlowManager.changeNextScreen();
        goTo("redirector");
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    /**
     * @param generatorBuilder the generatorBuilder to set
     */
    public void setGeneratorBuilder(GeneratorBuilder generatorBuilder) {
        this.generatorBuilder = generatorBuilder;
    }
    
    
    
}
