package com.navid.trafalgar.mod.common;

import com.navid.nifty.flow.ScreenFlowManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

/**
 * Created by alberto on 28/03/16.
 */
public abstract class WorkflowMenuController implements ScreenController {

    /**
     * From bind
     */
    protected Nifty nifty;
    /**
     * From bind
     */
    protected Screen screen;
    /**
     * Singleton
     */
    @Autowired
    private ScreenFlowManager screenFlowManager;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void next() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.NEXT);
        nifty.gotoScreen("redirector");
    }

    public void back() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.PREV);
        nifty.gotoScreen("redirector");
    }

    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }
}
