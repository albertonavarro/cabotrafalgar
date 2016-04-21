package com.navid.trafalgar.mod.tutorial.script.trigger;

import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.mod.tutorial.script.Actionable;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;
import com.navid.trafalgar.mod.tutorial.script.Trigger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Created by alberto on 17/04/16.
 */
public class EventTrigger implements Trigger {

    private EventManager eventManager;

    private String[] events;

    private EventListener eventListener = null;

    public EventTrigger(EventManager eventManager, String ... events) {
        this.eventManager = eventManager;
        this.events = events;
    }

    @Override
    public void register(final Actionable callable) {
        eventListener = eventManager.registerListener(new EventListener() {
            @Override
            public void onEvent(String event) {
                callable.action();
            }
        }, events);
    }

    @Override
    public void unregister() {
        eventManager.unregister(eventListener);
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
}
