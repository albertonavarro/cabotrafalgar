package com.navid.trafalgar.mod.common;

import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.audio.MusicManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

/**
 * Created by alberto on 11/08/16.
 */
public abstract class GameMenuController implements ScreenController {

    protected Nifty nifty;

    protected Screen screen;

    @Autowired
    private MusicManager musicManager;

    /**
     * Singleton
     */
    @Autowired
    protected ScreenFlowManager screenFlowManager;

    @Override
    public final void bind(@Nonnull Nifty nifty, @Nonnull Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public final void previous() {
    }

    public void next() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.NEXT);
        nifty.gotoScreen("redirector");
    }

    public void skip() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.SKIP);
        nifty.gotoScreen("redirector");
    }

    public final void back() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.PREV);
        nifty.gotoScreen("redirector");
    }

    public final void toggleVolume() {
        musicManager.toggleMute();
    }

    @Override
    public final void onStartScreen() {
        Label element = screen.findNiftyControl("musicFileName", Label.class);
        if(element != null) {
            element.setText(musicManager.getCurrentMusic().getName());
        }
        doOnStartScreen();
    }

    @Override
    public final void onEndScreen() {
        doOnEndScreen();
    }

    public abstract void doOnStartScreen();

    public abstract void doOnEndScreen();

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public final void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

    public final void setMusicManager(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

}
