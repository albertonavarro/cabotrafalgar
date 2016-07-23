package com.navid.trafalgar.mod.common;

import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.audio.MusicManager;
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

    private final String restartScreen;

    private final String quitScreen;

    protected GamePlayController(String restartScreen, String quitScreen) {
        this.restartScreen = restartScreen;
        this.quitScreen = quitScreen;
    }

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

    @Autowired
    protected MusicManager musicManager;

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

        screenFlowManager.setNextScreenHint(restartScreen);
        nifty.gotoScreen("redirector");
    }

    @Override
    public void quitGame() {
        showMenuFunction(false);
        eventManager.fireEvent(EventManager.FAILED);

        screenFlowManager.setNextScreenHint(quitScreen);
        nifty.gotoScreen("redirector");
    }

    @Override
    public void toggleControls() {

    }

    @Override
    public void toggleStats() {
        boolean newVisibility = !screen.findElementByName("panel_stats").isVisible();
        screen.findElementByName("panel_stats").setVisible(newVisibility);
    }

    @Override
    public void toggleMusic() {
        musicManager.toggleMute();
    }

    @Override
    public void toggleMenu() {
        boolean newVisibility = !screen.findElementByName("menuLayer").isVisible();
        showMenuFunction(newVisibility);
    }

    //PRIVATE SUPPORT METHODS

    public void showMenuFunction(boolean newVisibility) {
        screen.findElementByName("menuLayer").setVisible(newVisibility);
        if (newVisibility) {
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


    public void setMusicManager(MusicManager musicManager) {
        this.musicManager = musicManager;
    }
}
