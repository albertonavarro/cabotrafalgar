package com.navid.trafalgar.games;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class MyStartScreen extends AbstractAppState implements ScreenController {

    @Autowired
    private Nifty nifty;
    @Autowired
    private Application app;
    @Autowired
    private Screen screen;
    @Autowired
    private AppSettings settings;

    public void startGame(String nextScreen) {
        getNifty().gotoScreen(nextScreen);
    }

    public void quitGame() {
        getApp().stop();
    }

    /**
     * Nifty GUI ScreenControl methods
     */
    public void bind(Nifty nifty, Screen screen) {
        this.setNifty(nifty);
        this.setScreen(screen);
    }

    public void onStartScreen() {
        

    }

    public void onEndScreen() {
    }

    /**
     * jME3 AppState methods
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.setApp(app);
    }

    /**
     * @return the nifty
     */
    public Nifty getNifty() {
        return nifty;
    }

    /**
     * @param nifty the nifty to set
     */
    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }

    /**
     * @return the app
     */
    public Application getApp() {
        return app;
    }

    /**
     * @param app the app to set
     */
    public void setApp(Application app) {
        this.app = app;
    }

    /**
     * @return the screen
     */
    public Screen getScreen() {
        return screen;
    }

    /**
     * @param screen the screen to set
     */
    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    /**
     * @return the settings
     */
    public AppSettings getSettings() {
        return settings;
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(AppSettings settings) {
        this.settings = settings;
    }
}
