package com.navid.trafalgar.mod.common;

import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.audio.MusicManager;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.input.SystemInterpreter;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.statistics.AbstractStatistic;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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

    @Autowired
    private GeneratorBuilder generatorBuilder;

    @Override
    public final void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    //PUBLIC COMMANDS

    @Override
    public final void restartGame() {
        showMenuFunction(false);
        eventManager.fireEvent(EventManager.FAILED);

        screenFlowManager.setNextScreenHint(restartScreen);
        nifty.gotoScreen("redirector");
    }

    @Override
    public final void quitGame() {
        showMenuFunction(false);
        eventManager.fireEvent(EventManager.FAILED);

        screenFlowManager.setNextScreenHint(quitScreen);
        nifty.gotoScreen("redirector");
    }

    @Override
    public final void toggleControls() {
        boolean newVisibility = !screen.findElementByName("showControlLayer").isVisible();

        screen.findNiftyControl("showControlText", Label.class).setText(prettyPrintReport(generatorBuilder.generateReport()));
        screen.findElementByName("showControlLayer").setVisible(newVisibility);
    }

    @Override
    public final void toggleStats() {
        boolean newVisibility = !screen.findElementByName("panel_stats").isVisible();
        screen.findElementByName("panel_stats").setVisible(newVisibility);
    }

    @Override
    public final void toggleMusic() {
        musicManager.toggleMute();
    }

    @Override
    public final void toggleMenu() {
        boolean newVisibility = !screen.findElementByName("menuLayer").isVisible();
        showMenuFunction(newVisibility);
    }

    public final void updateShipStats() {
        ListBox listBox = screen.findNiftyControl("statsLists", ListBox.class);
        listBox.refresh();
    }

    public void fillShipStats(Collection<AbstractStatistic> stats) {
        ListBox listBox = screen.findNiftyControl("statsLists", ListBox.class);

        for (AbstractStatistic currentStat : stats) {
            listBox.addItem(currentStat);
        }
    }

    public final void clearStats() {
        ListBox listBox = screen.findNiftyControl("statsLists", ListBox.class);
        listBox.clear();
    }

    //PRIVATE SUPPORT METHODS

    public final void showMenuFunction(boolean newVisibility) {
        screen.findElementByName("menuLayer").setVisible(newVisibility);
        if (newVisibility) {
            eventManager.fireEvent("PAUSE");
        } else {
            eventManager.fireEvent("RESUME");
        }
    }

    private final String prettyPrintReport(Map<String, String> commands ) {
        List<String> keys = new ArrayList<>(commands.keySet());
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();

        for (String key : keys) {
            sb.append(key + ": " + commands.get(key) + "\n");
        }

        return sb.toString();
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


    public final void setMusicManager(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    public final void setGeneratorBuilder(GeneratorBuilder generatorBuilder) {
        this.generatorBuilder = generatorBuilder;
    }
}
