package com.navid.trafalgar.mod.windtunnel;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.system.AppSettings;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.StateManager;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.windtunnel.statelisteners.*;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.concurrent.Callable;

/**
 *
 * @author alberto
 */
public class WindTunnelMain  extends AbstractAppState implements ScreenController {
    
    /*
     * From bind
     */
    private Nifty nifty;
    
    /*
     * From bind
     */
    private Screen screen;
    /*
     * From initialize
     */
    private AppStateManager appStateManager;
    /*
     * From initialize
     */
    private Application app;
    //Shared among Listeners
    private final GameConfiguration gameConfiguration = new GameConfiguration();
    //State and Event manager
    private final EventManager eventManager = new EventManager();
    private final StateManager stateManager = new StateManager();
    private final StatisticsManager statisticsManager = new StatisticsManager();
    private final GameStatus gameStatus = new GameStatus(statisticsManager);

    private GUIUpdater guiUpdater;
    
    public WindTunnelMain(AppSettings settings) {
        stateManager.setEventManager(eventManager);
        gameConfiguration.setAppSettings(settings);

    }

    /** Nifty GUI ScreenControl methods */
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
        app.enqueue(new Callable<Void>() {

            public Void call() {
                guiUpdater = new GUIUpdater(screen, nifty, gameStatus, eventManager, app, statisticsManager);
                stateManager.reset();
                stateManager.register(new InitStateListener(app, gameStatus, gameConfiguration));
                stateManager.register(new LoadMapStateListener(app, eventManager, gameStatus, gameConfiguration, statisticsManager));
                stateManager.register(new LoadCameraStateListener(app, gameStatus, eventManager));
                stateManager.register(new LoadEventsListener(app));
                stateManager.register(new StartedListener(gameStatus, eventManager));
                stateManager.register(guiUpdater);
                setEnabled(true);
                return null;
            }
        });
    }

    public void onEndScreen() {
        app.enqueue(new Callable<Void>() {

            public Void call() {
                guiUpdater = null;
                
                eventManager.fireEvent(EventManager.UNLOAD);
                stateManager.update(0);
                
                setEnabled(false);
                
                return null;
            }
            
        });
    }

    /** jME3 AppState methods */
    @Override
    public void initialize(AppStateManager appStateManager, Application app) {
        this.appStateManager = appStateManager;
        this.app = app;

        //gameConfiguration.setModelBuilder(ModelBuilderFactory.getModelBuilder(gameConfiguration.getAppSettings(), app.getAssetManager(), eventManager));

        setEnabled(false);
    }

    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            stateManager.update(tpf);

        }

        gameStatus.getGameNode().updateGeometricState();
        gameStatus.getGameGUINode().updateGeometricState();
    }

    public void setGameConfiguration(GameConfiguration gc) {
        this.gameConfiguration.fromGameConfiguration(gc);
    }

    public void resume() {
        guiUpdater.resume();
    }

    public void restart() {
        guiUpdater.restart();
    }

    public void quit() {
        guiUpdater.quit();
    }
    
    public void showMenu(){
        guiUpdater.toggleMenu();
    }
}

