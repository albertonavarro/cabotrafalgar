package com.navid.trafalgar.mod.windtunnel.statelisteners;

import com.jme3.app.Application;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.manager.statistics.AbstractStatistic;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.model.AShipModel;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author alberto
 */
public class GUIUpdater implements StartedState, PrestartState {

    private Screen screen;
    private Nifty nifty;
    private GameStatus gameStatus;
    private final InputManager inputManager;
    private final EventManager eventManager;
    private final StatisticsManager statisticsManager;
    //Map elements
    private AShipModel ship;
    private boolean showMenu;

    public GUIUpdater(Screen screen, Nifty nifty, GameStatus gameStatus, EventManager eventManager, Application appSettings, StatisticsManager statisticsManager) {
        this.screen = screen;
        this.nifty = nifty;
        this.gameStatus = gameStatus;
        this.inputManager = appSettings.getInputManager();
        this.eventManager = eventManager;
        this.statisticsManager = statisticsManager;
    }

    public void onStarted(float tpf) {        
        updateShipStats();
    }

    public void onUnload() {
        inputManager.removeListener(actionListener);
        
        ListBox listBox = screen.findNiftyControl("statsLists", ListBox.class);
        listBox.clear();
    }

    public void onPrestart(float tpf) {
        ship = gameStatus.getPlayerNode();

        showMenu = false;

        inputManager.addListener(actionListener, "Menu"); // load my custom keybinding
        
        inputManager.addListener(new ActionListener() {

            public void onAction(String string, boolean bln, float f) {
                if(bln){
                    eventManager.fireEvent("DEBUG");
                }
            }
        }, "DEBUG");
        
        showMenuFunction(false); //nifty keeps the status, we need to reset it.

        fillShipStats(screen);
    }
    
    public void updateShipStats(){
        ListBox listBox = screen.findNiftyControl("statsLists", ListBox.class);
        listBox.refresh();
    }
    
    public void fillShipStats(Screen screen){
         ListBox listBox = screen.findNiftyControl("statsLists", ListBox.class);
         
         for(AbstractStatistic currentStat : statisticsManager.getAllStatistics().values()){
             listBox.addItem(currentStat);
         }

    }

    public void showMenuFunction(boolean value) {
        screen.findElementByName("menuLayer").setVisible(value);
        showMenu = value;
        if (value) {
            eventManager.fireEvent("PAUSE");
        } else {
            eventManager.fireEvent("RESUME");
        }
    }
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed) {
                if (name.equals("Menu")) {
                    toggleMenu();
                }
            }
        }
    };
    
    synchronized public void toggleMenu() {
        showMenu = !showMenu;
        showMenuFunction(showMenu);
    }
    
    synchronized public void resume() {
        showMenuFunction(false);
    }

    synchronized public void restart() {
        showMenuFunction(false);
        eventManager.fireEvent(EventManager.FAILED);
        nifty.gotoScreen("windTunnelScreen");
    }

    synchronized public void quit() {
        showMenuFunction(false);
        eventManager.fireEvent(EventManager.FAILED);
        nifty.gotoScreen("preWindTunnelScreen");
    }
}
