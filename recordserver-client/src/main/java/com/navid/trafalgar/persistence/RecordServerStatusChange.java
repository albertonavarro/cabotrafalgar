package com.navid.trafalgar.persistence;

import com.navid.trafalgar.events.ServerStatusEvent;

/**
 * Created by anavarro on 09/02/17.
 */
public class RecordServerStatusChange extends ServerStatusEvent {


    private final RecordPersistenceService.Status newStatus;

    public RecordServerStatusChange( RecordPersistenceService.Status newStatus) {
        this.newStatus = newStatus;
    }

    public RecordPersistenceService.Status getNewStatus() {
        return newStatus;
    }
}
