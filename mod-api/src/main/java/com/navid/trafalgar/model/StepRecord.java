package com.navid.trafalgar.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anf
 */
public abstract class StepRecord {

    private static final int version = 1;

    private float timestamp;

    private List<String> eventList = new ArrayList();

    /**
     * @return the version
     */
    public static int getVersion() {
        return version;
    }

    /**
     * @return the timestamp
     */
    public float getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the eventList
     */
    public List<String> getEventList() {
        return eventList;
    }

    /**
     * @param eventList the eventList to set
     */
    public void setEventList(List<String> eventList) {
        this.eventList = eventList;
    }

}
