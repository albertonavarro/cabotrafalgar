package com.navid.trafalgar.mod.windtunnel.statelisteners;

import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;
import static com.navid.trafalgar.manager.EventManager.MILLESTONE_REACHED;
import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.mod.windtunnel.WindTunnelGameModel;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.GameStatus;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author anf
 */
public class StartedListener implements PrestartState, StartedState {

    @Autowired
    private GameStatus gameStatus;

    @Override
    public void onPrestart(float tpf) {
        gameStatus.getTime().setValue(0f);
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
}
