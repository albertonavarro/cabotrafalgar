package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.counterclock.CounterClockMainScreen;
import com.navid.trafalgar.model.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class GUIUpdater implements StartedState, PrestartState {

    @Autowired
    private CounterClockMainScreen mainScreen;
    @Autowired
    private InputManager inputManager;
    @Autowired
    private StatisticsManager statisticsManager;


    public void onStarted(float tpf) {
        mainScreen.updateShipStats();
    }

    public void onUnload() {
        inputManager.removeListener(actionListener);

        mainScreen.clearStats();
    }

    public void onPrestart(float tpf) {
        inputManager.addListener(actionListener, "Menu"); // load my custom keybinding
        mainScreen.fillShipStats(statisticsManager.getAllStatistics().values());
    }

    
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed) {
                if (name.equals("Menu")) {
                    mainScreen.toggleMenu();
                }
            }
        }
    };

    public void resume() {
        mainScreen.showMenuFunction(false);
    }

    public void restart() {
        mainScreen.restart();
    }

    public void quit() {
        mainScreen.quit();
    }

    /**
     * @param mainScreen the mainScreen to set
     */
    public void setMainScreen(CounterClockMainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }


    /**
     * @param inputManager the inputManager to set
     */
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    /**
     * @param statisticsManager the statisticsManager to set
     */
    public void setStatisticsManager(StatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }
}
