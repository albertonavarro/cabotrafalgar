package com.navid.trafalgar.lazylogin;

import com.navid.trafalgar.events.ServerStatusEvent;

/**
 * Created by anavarro on 12/02/17.
 */
public class LazyLoginServerStatusChange extends ServerStatusEvent {

    private final LazyLoginService.Status newStatus;

    public LazyLoginServerStatusChange( LazyLoginService.Status newStatus) {
        this.newStatus = newStatus;
    }

    public LazyLoginService.Status getNewStatus() {
        return newStatus;
    }
}
