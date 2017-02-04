/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.games;

import com.jme3.app.Application;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.audio.MusicManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

public final class RootScreenController implements ScreenController {

    @Autowired
    private Application application;
    @Autowired
    private ScreenFlowManager screenFlowManager;
    @Autowired
    private MusicManager musicManager;

    /*
     From Bind
     */
    private Nifty nifty;
    /*
     From Bind
     */
    private Screen screen;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        musicManager.setCurrentAmbient("menu");
    }

    @Override
    public void onEndScreen() {
    }

    public void executeModule(String moduleName) {
        screenFlowManager.setNextScreenHint(moduleName);
        nifty.gotoScreen("redirector");
    }

    public void quit() {
        application.stop(false);
        System.exit(0);
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    public void setMusicManager(MusicManager musicManager) {
        this.musicManager = musicManager;
    }
}
