package com.navid.trafalgar.mod.tutorial.script;

import java.util.Optional;

public class ScriptEvent {

    private Trigger trigger;

    private Actionable action;

    private Optional<Long> timeoutMillis = Optional.empty();

    private String[] successEvent = new String[] {"SCRIPT_STEP_ACTIONED"};

    private Optional<Boolean> successful = Optional.empty();

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

    public Optional<Long> getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(Optional<Long> timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = Optional.of(successful);
    }

    public Optional<Boolean> isSuccessful() {
        return successful;
    }
}
