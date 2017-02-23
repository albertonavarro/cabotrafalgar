package com.navid.trafalgar.gamemanager;

import com.navid.trafalgar.events.ServerStatusEvent;

/**
 * Created by anavarro on 12/02/17.
 */
public class GameManagerServerStatusChange extends ServerStatusEvent {

    private final GameManagerService.Status newStatus;

    public GameManagerServerStatusChange(GameManagerService.Status newStatus) {
        this.newStatus = newStatus;
    }

    public GameManagerService.Status getNewStatus() {
        return newStatus;
    }
}
