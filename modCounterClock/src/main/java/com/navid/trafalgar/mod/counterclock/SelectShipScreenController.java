package com.navid.trafalgar.mod.counterclock;

import com.navid.trafalgar.persistence.RecordServerStatusChange;
import com.navid.trafalgar.persistence.recordserver.RecordServerPersistenceService;
import de.lessvoid.nifty.controls.Label;
import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.collect.Lists.newArrayList;

public final class SelectShipScreenController extends com.navid.trafalgar.mod.common.SelectShipScreenController {

    @Autowired
    private EventService eventService;

    private EventSubscriber<RecordServerStatusChange> subscriber = null;

    @Autowired
    private RecordServerPersistenceService recordServerPersistenceService;

    @Override
    public void doOnStartScreen() {
        super.doOnStartScreen();

        final Label elementRecordServerStatus = screen.findNiftyControl("recordServerStatus", Label.class);
        if(elementRecordServerStatus != null) {
            elementRecordServerStatus.setText(recordServerPersistenceService.getStatus().name());
        }

        subscriber = new EventSubscriber<RecordServerStatusChange>() {
            @Override
            public void onEvent(RecordServerStatusChange event) {
                if (elementRecordServerStatus != null) {
                    elementRecordServerStatus.setText(event.getNewStatus().name());
                }
            }
        };

        eventService.subscribe(RecordServerStatusChange.class, subscriber);
    }

    @Override
    public void doOnEndScreen() {
        super.doOnEndScreen();
        eventService.unsubscribe(RecordServerStatusChange.class, subscriber);
    }

    public final void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setRecordServerPersistenceService(RecordServerPersistenceService recordServerPersistenceService) {
        this.recordServerPersistenceService = recordServerPersistenceService;
    }
}
