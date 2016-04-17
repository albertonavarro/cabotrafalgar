package com.navid.trafalgar.mod.tutorial.script;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by alberto on 17/04/16.
 */
public class EventTrigger implements Trigger {

    private Collection<String> events;

    public EventTrigger(String ... events) {
        this.events = Arrays.asList(events);
    }



}
