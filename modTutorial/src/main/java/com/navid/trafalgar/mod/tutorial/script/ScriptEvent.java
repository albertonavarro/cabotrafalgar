package com.navid.trafalgar.mod.tutorial.script;

public class ScriptEvent {

    private Trigger trigger;

    private Actionable action;

    private String[] successEvent = new String[] {"SCRIPT_STEP_ACTIONED"};

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

    public String[] getSuccessEvent() {
        return successEvent;
    }

    public void setSuccessEvent(String[] successEvent) {
        this.successEvent = successEvent;
    }
}
