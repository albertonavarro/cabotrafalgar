package com.navid.trafalgar.model;

import java.util.Collection;
import java.util.List;

public interface GameModelInterface {

    /**
     * Adds objects
     * @param collection
     */
    void addToModel(Collection collection);

    /**
     * Adds objects with a given name for all them
     * @param collection
     * @param name
     */
    void addToModel(Collection collection, String name);

    /**
     *
     * @param <T>
     * @param className
     * @return
     */
    <T> List<T> getByType(Class<T> className);

    /**
     *
     * @param <T>
     * @param className
     * @return
     */
    <T> T getSingleByType(Class<T> className);

    /**
     *
     * @param tClass
     * @param name
     * @param <T>
     * @return
     */
    <T> T getSingleByTypeAndName(Class<T> tClass, String name);

    void removeFromModel(Class className);

}
