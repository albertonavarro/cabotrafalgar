package com.navid.trafalgar.model;

/**
 * This interface is implemented by those objects that need any dependency from GameModel.
 * 
 * @see Builder2
 */
public interface Dependent {
    
    /**
     * This method is invoked by Builder2 after all objects have been created.
     *
     * @param gameModel
     */
    public void resolveDependencies(GameModel gameModel);
    
}
