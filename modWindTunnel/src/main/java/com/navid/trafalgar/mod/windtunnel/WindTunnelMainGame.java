package com.navid.trafalgar.mod.windtunnel;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.StateListener;
import com.navid.trafalgar.manager.StateManager;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameStatus;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public final class WindTunnelMainGame extends AbstractAppState {

    /*
     * EventManager (Broadcasting of events)
     */
    @Autowired
    private EventManager eventManager;
    /*
     * StateManager for this game iteration
     */
    @Autowired
    private StateManager stateManager;
    /**
     * GameStatus (Cameras, ViewPorts, Nodes, GameDefinition and time)
     */
    @Autowired
    private GameStatus gameStatus;
    /**
     * StateListeners for CounterClock
     */
    @Autowired
    private List<StateListener> stateListeners;

    @Autowired
    private GameConfiguration gameConfiguration;

    /**
     * jME3 AppState methods
     * @param appStateManager
     * @param app
     */
    @Override
    public final void initialize(final AppStateManager appStateManager, final Application app) {
        stateManager.reset();

        for (StateListener currentStateListener : stateListeners) {
            stateManager.register(currentStateListener);
        }

        for (StateListener currentStateListener : gameConfiguration.getPreGameModel().getByType(StateListener.class)) {
            stateManager.register(currentStateListener);
        }

        setEnabled(true);
    }

    @Override
    public final void stateDetached(final AppStateManager unused) {
        eventManager.fireEvent(EventManager.UNLOAD);
        setEnabled(false);
    }

    @Override
    public final void update(final float tpf) {
        if (isEnabled()) {
            stateManager.update(tpf);
        }

        gameStatus.getGameNode().updateGeometricState();
        gameStatus.getGameGUINode().updateGeometricState();
    }

    /**
     * @param inEventManager
     */
    public final void setEventManager(final EventManager inEventManager) {
        this.eventManager = inEventManager;
    }

    /**
     * @param inStateManager the stateManager to set
     */
    public final void setStateManager(final StateManager inStateManager) {
        this.stateManager = inStateManager;
    }

    /**
     * @param inGameStatus the gameStatus to set
     */
    public final void setGameStatus(final GameStatus inGameStatus) {
        this.gameStatus = inGameStatus;
    }

    /**
     * @param inStateListeners the stateListeners to set
     */
    public final void setStateListeners(final List<StateListener> inStateListeners) {
        this.stateListeners = inStateListeners;
    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

}
