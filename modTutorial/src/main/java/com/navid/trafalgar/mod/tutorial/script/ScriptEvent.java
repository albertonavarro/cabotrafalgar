package com.navid.trafalgar.mod.tutorial.script;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by alberto on 17/04/16.
 */
public class ScriptEvent {

    private Collection<String> messages = new ArrayList<String>();

    private Trigger trigger;

    private Optional<Timeout> timeoutInfo = Optional.absent();

    private Collection<String> onSuccessThrowEvents = new ArrayList<String>();

    public Collection<String> getMessages() {
        return messages;
    }

    public void setMessages(Collection<String> messages) {
        this.messages = messages;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Optional<Timeout> getTimeoutInfo() {
        return timeoutInfo;
    }

    public void setTimeoutInfo(Optional<Timeout> timeoutInfo) {
        this.timeoutInfo = timeoutInfo;
    }

    public Collection<String> getOnSuccessThrowEvents() {
        return onSuccessThrowEvents;
    }

    public void setOnSuccessThrowEvents(Collection<String> onSuccessThrowEvents) {
        this.onSuccessThrowEvents = onSuccessThrowEvents;
    }
}
