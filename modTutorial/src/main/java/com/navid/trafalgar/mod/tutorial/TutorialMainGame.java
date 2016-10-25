package com.navid.trafalgar.mod.tutorial;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.StateListener;
import com.navid.trafalgar.manager.StateManager;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public final class TutorialMainGame extends AbstractAppState {

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
     */
    @Override
    public void initialize(final AppStateManager appStateManager, final Application app) {
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
    public void stateDetached(final AppStateManager unused) {
        eventManager.fireEvent(EventManager.UNLOAD);
        setEnabled(false);
    }

    @Override
    public void update(final float tpf) {
        if (isEnabled()) {
            stateManager.update(tpf);
        }

        gameStatus.getGameNode().updateGeometricState();
        gameStatus.getGameGUINode().updateGeometricState();
    }

    public void setEventManager(final EventManager inEventManager) {
        this.eventManager = inEventManager;
    }

    public void setStateManager(final StateManager inStateManager) {
        this.stateManager = inStateManager;
    }

    public void setGameStatus(final GameStatus inGameStatus) {
        this.gameStatus = inGameStatus;
    }

    public void setStateListeners(final List<StateListener> inStateListeners) {
        this.stateListeners = inStateListeners;
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

}
