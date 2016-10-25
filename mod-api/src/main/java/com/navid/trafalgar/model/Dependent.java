package com.navid.trafalgar.model;

/**
 *
 *
 */
public interface Dependent {

    /**
     *
     * @param gameModel
     */
    void resolveDependencies(GameModel gameModel);

    void commitDependencies();

}
