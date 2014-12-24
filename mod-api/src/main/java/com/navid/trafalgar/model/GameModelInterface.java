package com.navid.trafalgar.model;

import java.util.Collection;
import java.util.List;

public interface GameModelInterface {

    /**
     *
     * @param collection
     */
    void addToModel(Collection collection);

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

    void removeFromModel(Class className);

}
