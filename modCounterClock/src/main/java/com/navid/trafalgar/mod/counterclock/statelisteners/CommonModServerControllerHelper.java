package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.navid.trafalgar.gamemanager.GameManagerServerStatusChange;
import com.navid.trafalgar.gamemanager.GameManagerService;
import com.navid.trafalgar.lazylogin.LazyLoginServerStatusChange;
import com.navid.trafalgar.lazylogin.LazyLoginService;
import com.navid.trafalgar.persistence.RecordServerStatusChange;
import com.navid.trafalgar.persistence.recordserver.RecordServerPersistenceService;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventSubscriber;

import javax.annotation.Nonnull;

/**
 * Created by anavarro on 14/02/17.
 */
public class CommonModServerControllerHelper {

    private final EventService eventService;

    private final EventSubscriber<RecordServerStatusChange> subscriber;
    private final EventSubscriber<LazyLoginServerStatusChange> subscriberLazyLogin;
    private final EventSubscriber<GameManagerServerStatusChange> subscriberGameManager;


    public CommonModServerControllerHelper(EventService eventService, RecordServerPersistenceService recordServerPersistenceService, LazyLoginService lazyLoginService, GameManagerService gameManagerService, Screen screen) {
        this.eventService = eventService;

        final Label elementGameManagerStatus = screen.findNiftyControl("gameManagerStatus", Label.class);
        if(elementGameManagerStatus != null) {
            elementGameManagerStatus.setText(gameManagerService.getStatus().name());
        }

        subscriberGameManager = new EventSubscriber<GameManagerServerStatusChange>() {
            @Override
            public void onEvent(GameManagerServerStatusChange event) {
                if (elementGameManagerStatus != null) {
                    elementGameManagerStatus.setText(event.getNewStatus().name());
                }
            }
        };

        final Label elementLazyloginStatus = screen.findNiftyControl("lazyLoginStatus", Label.class);
        if(elementLazyloginStatus != null) {
            elementLazyloginStatus.setText(lazyLoginService.getStatus().name());
        }

        subscriberLazyLogin = new EventSubscriber<LazyLoginServerStatusChange>() {
            @Override
            public void onEvent(LazyLoginServerStatusChange event) {
                if (elementLazyloginStatus != null) {
                    elementLazyloginStatus.setText(event.getNewStatus().name());
                }
            }
        };

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

        eventService.subscribe(LazyLoginServerStatusChange.class, subscriberLazyLogin);

        eventService.subscribe(RecordServerStatusChange.class, subscriber);

        eventService.subscribe(GameManagerServerStatusChange.class, subscriberGameManager);

    }

    public void destroy() {
        eventService.unsubscribe(RecordServerStatusChange.class, subscriber);
        eventService.unsubscribe(LazyLoginServerStatusChange.class, subscriberLazyLogin);
        eventService.unsubscribe(GameManagerServerStatusChange.class, subscriberGameManager);

    }

}
