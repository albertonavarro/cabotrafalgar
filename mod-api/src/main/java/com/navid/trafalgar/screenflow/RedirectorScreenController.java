package com.navid.trafalgar.screenflow;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

public final class RedirectorScreenController implements ScreenController {

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
        nifty.gotoScreen(screenFlowManager.nextScreen());
    }

    @Override
    public void onEndScreen() {
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

}
