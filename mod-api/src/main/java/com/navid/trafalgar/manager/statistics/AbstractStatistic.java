package com.navid.trafalgar.manager.statistics;

/**
 * This class represents a Statistic to be evaluated.
 *
 * @param <T>
 */
public abstract class AbstractStatistic<T> {

    /*
     * Id for the statistic
     */
    private final String id;
    /*
     * Logical parent id
     */
    private final String parentId;

    /*
     * Constructor
     */
    /**
     *
     * @param parentId
     * @param id
     */
    protected AbstractStatistic(String parentId, String id) {
        this.id = id;
        this.parentId = parentId;
    }

    /*
     * Get parentid
     */
    /**
     *
     * @return
     */
    public final String getParentId() {
        return parentId;
    }

    /*
     * Get Id
     */
    /**
     *
     * @return
     */
    public final String getId() {
        return id;
    }

    /*
     * Get typed value
     */
    /**
     *
     * @return
     */
    public abstract T getValue();

    /*
     * Set typed value
     */
    /**
     *
     * @param newValue
     */
    public abstract void setValue(T newValue);

}
