package com.navid.trafalgar.mod.tutorial.script;

import java.util.concurrent.Callable;

public class ScriptEvent {

    private Trigger trigger;

    private Actionable action;

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Actionable getAction() {
        return action;
    }

    public void setAction(Actionable action) {
        this.action = action;
    }
}
