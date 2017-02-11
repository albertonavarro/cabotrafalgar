package com.navid.trafalgar.mod.counterclock;

import com.navid.lazylogin.context.RequestContextContainer;
import com.navid.trafalgar.mod.common.GameMenuController;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.navid.trafalgar.persistence.RecordServerStatusChange;
import com.navid.trafalgar.persistence.recordserver.RecordServerPersistenceService;
import com.navid.trafalgar.profiles.ProfileManager;
import com.navid.trafalgar.profiles.ProfileStatus;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public final class SelectProfileScreenController extends com.navid.trafalgar.mod.common.SelectProfileScreenController {

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
