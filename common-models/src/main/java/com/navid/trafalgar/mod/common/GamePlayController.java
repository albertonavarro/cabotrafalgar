package com.navid.trafalgar.mod.common;

import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.input.SystemInterpreter;
import com.navid.trafalgar.manager.EventManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alberto on 17/07/16.
 */
public abstract class GamePlayController implements ScreenController, SystemInterpreter, GamePlayScreenCommands {

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
    protected ScreenFlowManager screenFlowManager;

    @Autowired
    protected EventManager eventManager;


    private boolean showMenu;



    @Override
    public final void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    //PUBLIC COMMANDS

    @Override
    public void restartGame() {
        showMenuFunction(false);
        eventManager.fireEvent(EventManager.FAILED);

        screenFlowManager.setNextScreenHint("windTunnelScreen");
        nifty.gotoScreen("redirector");
    }

    @Override
    public void quitGame() {
        showMenuFunction(false);
        eventManager.fireEvent(EventManager.FAILED);

        screenFlowManager.setNextScreenHint("selectShip");
        nifty.gotoScreen("redirector");
    }

    @Override
    public void showControls() {

    }

    @Override
    public void showStats() {

    }

    @Override
    public void toggleMusic() {

    }

    @Override
    public void toggleMenu() {
        showMenu = !showMenu;
        showMenuFunction(showMenu);
    }

    //PRIVATE SUPPORT METHODS

    public void showMenuFunction(boolean value) {
        screen.findElementByName("menuLayer").setVisible(value);
        showMenu = value;
        if (value) {
            eventManager.fireEvent("PAUSE");
        } else {
            eventManager.fireEvent("RESUME");
        }
    }


    //VARIABLE SETTERS


    /**
     *
     * @param screenFlowManager
     */
    public final void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

    /**
     * @param eventManager the eventManager to set
     */
    public final void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }


}
