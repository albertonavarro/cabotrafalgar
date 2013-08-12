/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.games;

import com.navid.trafalgar.screenflow.ScreenFlowManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class StartScreenController implements ScreenController {

    @Autowired
    private ScreenFlowManager screenFlowManager;
    private Nifty nifty;
    private Screen screen;

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

    public void executeModule(String moduleName) {
        screenFlowManager.changeFlow(moduleName);
        nifty.gotoScreen("redirector");
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }
    
    
}
