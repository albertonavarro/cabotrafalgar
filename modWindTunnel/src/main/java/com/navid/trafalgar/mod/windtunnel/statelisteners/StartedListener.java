package com.navid.trafalgar.mod.windtunnel.statelisteners;

import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;
import static com.navid.trafalgar.manager.EventManager.MILLESTONE_REACHED;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.model.mod.counterclock.AMillestoneModel;
import com.navid.trafalgar.model.AShipModel;
import java.util.Collection;

/**
 *
 * @author anf
 */
public class StartedListener implements PrestartState, StartedState {

    private float time = 0;
    private GameStatus gameStatus;
    private AShipModel ship;
    private Collection<AMillestoneModel> millestones;
    private EventManager eventManager;

    public StartedListener(GameStatus gameStatus, EventManager eventManager) {
        this.gameStatus = gameStatus;
        this.eventManager = eventManager;

        eventManager.registerListener(new MillestoneEventHandler(), new String[]{MILLESTONE_REACHED});
    }

    public void onPrestart(float tpf) {
        ship = gameStatus.getPlayerNode();
        millestones = gameStatus.getMillestoneGameModel().getMillestones();

    }

    public void onStarted(float tpf) {
        gameStatus.getTime().setValue(gameStatus.getTime().getValue() + tpf);

        gameStatus.getGameNode().updateLogicalState(tpf);
        gameStatus.getGameGUINode().updateLogicalState(tpf);
    }

    public void onUnload() {
    }

    private class MillestoneEventHandler implements EventListener {

        int counter = 0;

        public void onEvent(final String event) {
            if (MILLESTONE_REACHED.equals(event)) {
                counter++;
            }

            if (counter == millestones.size()) {
                eventManager.fireEvent(EventManager.SUCCESSFUL);
            }
        }
    }
}
