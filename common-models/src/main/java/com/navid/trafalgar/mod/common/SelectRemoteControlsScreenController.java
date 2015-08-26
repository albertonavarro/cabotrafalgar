package com.navid.trafalgar.mod.common;

import com.navid.nifty.flow.ScreenFlowManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alberto on 8/5/15.
 */
public class SelectRemoteControlsScreenController implements ScreenController {

    /**
     * From bind
     */
    private Nifty nifty;
    /**
     * From bind
     */
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

    @Autowired
    private ScreenFlowManager screenFlowManager;

    public void next() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.NEXT);
        nifty.gotoScreen("redirector");
    }

    public void back() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.PREV);
        nifty.gotoScreen("redirector");
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }
}
