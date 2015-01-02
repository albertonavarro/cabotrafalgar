package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;
import static com.navid.trafalgar.manager.EventManager.MILLESTONE_REACHED;
import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.mod.counterclock.CounterClockGameModel;
import com.navid.trafalgar.mod.counterclock.model.AMilestoneModel;
import com.navid.trafalgar.model.GameStatus;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

public final class StartedListener implements PrestartState, StartedState {

    @Autowired
    private GameStatus gameStatus;
    private Collection<AMilestoneModel> milestones;
    @Autowired
    private EventManager eventManager;

    @Autowired
    private CounterClockGameModel model;

    @Override
    public void onPrestart(float tpf) {
        gameStatus.getTime().setValue(0f);
        milestones = model.getMilestones();

    }

    @Override
    public void onStarted(float tpf) {
        gameStatus.getTime().setValue(gameStatus.getTime().getValue() + tpf);

        //This shouldn't be here, but we need to tell jME to not update Controls if we are on pause
        gameStatus.getGameNode().updateLogicalState(tpf);
        gameStatus.getGameGUINode().updateLogicalState(tpf);
    }

    @Override
    public void onUnload() {
    }

    /**
     * @param gameStatus the gameStatus to set
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * @param eventManager the eventManager to set
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        eventManager.registerListener(new MilestoneEventHandler(), new String[]{MILLESTONE_REACHED});
    }

    /**
     * @param model the model to set
     */
    public void setModel(CounterClockGameModel model) {
        this.model = model;
    }

    private class MilestoneEventHandler implements EventListener {

        private int counter = 0;

        @Override
        public void onEvent(final String event) {
            if (MILLESTONE_REACHED.equals(event)) {
                counter++;
            }

            if (counter == milestones.size()) {
                eventManager.fireEvent(EventManager.SUCCESSFUL);
            }
        }
    }
}
