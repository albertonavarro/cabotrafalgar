package com.navid.trafalgar.manager.statistics;

/**
 *
 * @author alberto
 */
public abstract class AbstractStatistic<T> {

    private final String id;
    private final String parentId;
    
    protected AbstractStatistic(String parentId, String id){
        this.id = id;
        this.parentId = parentId;
    }
    
    public String getParentId() {
        return parentId;
    }

    public String getId() {
        return id;
    }
    
    public abstract T getValue();

    public abstract void setValue(T newValue);
    
}
