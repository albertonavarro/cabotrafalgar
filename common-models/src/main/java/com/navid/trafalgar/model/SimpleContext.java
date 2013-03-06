package com.navid.trafalgar.model;

/**
 *
 * @author alberto
 */
public class SimpleContext implements IContext, Dependent{
    private IWind iWind;
    
    private IWater iWater;

    /**
     * @return the iWind
     */
    @Override
    public IWind getWind() {
        return iWind;
    }

    /**
     * @param iWind the iWind to set
     */
    public void setWind(IWind iWind) {
        this.iWind = iWind;
    }

    /**
     * @return the iWater
     */
    @Override
    public IWater getWater() {
        return iWater;
    }

    /**
     * @param iWater the iWater to set
     */
    public void setWater(IWater iWater) {
        this.iWater = iWater;
    }

    @Override
    public void resolveDependencies(GameModel gameModel) {
        setWind(gameModel.getSingleByType(IWind.class));
    }
    
}
