package com.navid.trafalgar.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author anf
 * @param <T>
 */
public abstract class CandidateRecord<T extends StepRecord> {
    
    private final int version = 1;
    
    private final Header header = new Header();
    
    private final List<T> stepRecordList = new LinkedList<T>();
   
    public CandidateRecord() {
        
    }
    
    public void addStepRecord( T stepRecord ){
        this.stepRecordList.add(stepRecord);
    }

    public Header getHeader() {
        return this.header;
    }
    
    public float getTime(){
        if (! stepRecordList.isEmpty()){
            return stepRecordList.get(stepRecordList.size()-1).getTimestamp();
        }
        
        return 0;
    }
    
    public List<? extends StepRecord> getStepRecord(){
        return stepRecordList;
    }

    public void setMap(String map) {
        this.header.setMap(map);
    }

    
    
}
