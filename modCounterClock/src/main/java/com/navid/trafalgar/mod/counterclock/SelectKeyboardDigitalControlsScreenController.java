package com.navid.trafalgar.mod.counterclock;

import com.navid.trafalgar.gamemanager.GameManagerService;
import com.navid.trafalgar.lazylogin.LazyLoginService;
import com.navid.trafalgar.mod.counterclock.statelisteners.CommonModServerControllerHelper;
import com.navid.trafalgar.persistence.RecordServerStatusChange;
import com.navid.trafalgar.persistence.recordserver.RecordServerPersistenceService;
import de.lessvoid.nifty.controls.Label;
import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;

public final class SelectKeyboardDigitalControlsScreenController extends com.navid.trafalgar.mod.common.SelectKeyboardDigitalControlsScreenController {
    @Autowired
    private EventService eventService;

    @Autowired
    private RecordServerPersistenceService recordServerPersistenceService;

    @Autowired
    private LazyLoginService lazyLoginService;

    @Autowired
    private GameManagerService gameManagerService;

    private CommonModServerControllerHelper modHelper;

    @Override
    public void doOnStartScreen() {
        super.doOnStartScreen();
        modHelper = new CommonModServerControllerHelper(eventService, recordServerPersistenceService, lazyLoginService, gameManagerService, screen);
    }

    @Override
    public void doOnEndScreen() {
        super.doOnEndScreen();
        modHelper.destroy();
    }

    public final void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setRecordServerPersistenceService(RecordServerPersistenceService recordServerPersistenceService) {
        this.recordServerPersistenceService = recordServerPersistenceService;
    }

    public void setLazyLoginService(LazyLoginService lazyLoginService) {
        this.lazyLoginService = lazyLoginService;
    }

    public void setGameManagerService(GameManagerService gameManagerService) {
        this.gameManagerService = gameManagerService;
    }
}
