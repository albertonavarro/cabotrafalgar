package com.navid.trafalgar.mod.tutorial.script.trigger;

import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.mod.tutorial.script.Actionable;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;
import com.navid.trafalgar.mod.tutorial.script.Trigger;

/**
 * Created by alberto on 17/04/16.
 */
public class TimeSinceLastEvent implements Trigger {

    private EventManager eventManager;

    private String[] events;

    private long startingEvent;

    private long delay_milliseconds;

    public TimeSinceLastEvent(EventManager eventManager, long milliseconds, String ... events) {
        this.eventManager = eventManager;
        this.events = events;
        this.delay_milliseconds = milliseconds;
    }

    public TimeSinceLastEvent(long milliseconds, String ... events) {
        this.eventManager = eventManager;
        this.events = events;
        this.delay_milliseconds = milliseconds;
    }

    @Override
    public void register(final Actionable callable) {

        startingEvent = System.currentTimeMillis();

        eventManager.registerListener(new EventListener() {
            @Override
            public void onEvent(String event) {
                if(startingEvent + delay_milliseconds < System.currentTimeMillis()) {
                    System.out.println("startingEvent: " + startingEvent + ", delay_milliseconds: " + delay_milliseconds + ", sum: " + (startingEvent + delay_milliseconds) + ", currentTimeMillis: " + System.currentTimeMillis());
                    eventManager.unregister(this);
                    eventManager.registerListener(new EventListener() {
                        @Override
                        public void onEvent(String event) {
                            callable.action();
                            eventManager.unregister(this);
                        }
                    }, new String[] { "FRAME_STEP" });
                }
            }
        }, new String[] { "FRAME_STEP" });
    }

    @Override
    public void unregister() {
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void setScriptInterpreter(ScriptInterpreter scriptInterpreter) {

    }
}
