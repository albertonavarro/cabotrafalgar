package com.navid.trafalgar.mod.windtunnel.statelisteners;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.windtunnel.WindTunnelMainScreen;
import org.springframework.beans.factory.annotation.Autowired;

public final class GUIUpdater implements StartedState, PrestartState {

    @Autowired
    private WindTunnelMainScreen mainScreen;
    @Autowired
    private InputManager inputManager;
    @Autowired
    private StatisticsManager statisticsManager;

    @Override
    public void onStarted(float tpf) {
        mainScreen.updateShipStats();
    }

    @Override
    public void onUnload() {
        inputManager.removeListener(actionListener);

        mainScreen.clearStats();
    }

    @Override
    public void onPrestart(float tpf) {
        inputManager.addListener(actionListener, "Menu"); // load my custom keybinding
        mainScreen.fillShipStats(statisticsManager.getAllStatistics().values());
    }

    private final ActionListener actionListener = new ActionListener() {

        @Override
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
        mainScreen.restartGame();
    }

    public void quit() {
        mainScreen.quitGame();
    }

    /**
     * @param mainScreen the mainScreen to set
     */
    public void setMainScreen(WindTunnelMainScreen mainScreen) {
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
