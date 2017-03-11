package com.navid.trafalgar.mod.tutorial.script.action;

import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.mod.tutorial.script.Actionable;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;

/**
 * Created by anavarro on 26/02/17.
 */
public class EventActionable extends Actionable {

    private final String[] events;

    public EventActionable(ScriptInterpreter scriptInterpreter, EventManager eventManager, String[] events) {
        super(scriptInterpreter, eventManager);
        this.events = events;
    }

    @Override
    public void action() {
        for(String event : events) {
            eventManager.fireEvent(event);
        }

        eventManager.fireEvent("SCRIPT_STEP_ACTIONED");
    }

    @Override
    public void cleanUpAction() {
    }
}
