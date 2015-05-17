package com.navid.trafalgar.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author anf
 * @param <T>
 */
public class CandidateRecord<T extends StepRecord> {

    private final int version = 1;

    private final Header header = new Header();

    private final List<T> stepRecordList = new LinkedList<T>();

    public CandidateRecord() {

    }

    public final void addStepRecord(T stepRecord) {
        this.stepRecordList.add(stepRecord);
    }

    public final Header getHeader() {
        return this.header;
    }

    public final float getTime() {
        if (!stepRecordList.isEmpty()) {
            return stepRecordList.get(stepRecordList.size() - 1).getTimestamp();
        }

        return 0;
    }

    public final List<? extends StepRecord> getStepRecord() {
        return stepRecordList;
    }

    public final void setMap(String map) {
        this.header.setMap(map);
    }

}
