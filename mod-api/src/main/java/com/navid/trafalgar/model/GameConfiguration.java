package com.navid.trafalgar.model;

import com.jme3.system.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 *
 */
public final class GameConfiguration {

    private String map;

    @Autowired
    private AppSettings appSettings;

    private String shipName;

    private GameModel preGameModel;

    public GameConfiguration() {
        reset();
    }

    public void reset() {
        preGameModel = new GameModel();
        shipName = null;
    }

    /**
     * @return the map
     */
    public String getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(String map) {
        this.map = map;
    }

    /**
     *
     * @param gc
     */
    public void fromGameConfiguration(GameConfiguration gc) {
        if (gc.getMap() != null) {
            this.setMap(gc.getMap());
        }

        if (gc.getAppSettings() != null) {
            this.setAppSettings(gc.getAppSettings());
        }

        if (gc.getShipName() != null) {
            this.setShipName(gc.getShipName());
        }
    }

    /**
     * @return the appSettings
     */
    public AppSettings getAppSettings() {
        return appSettings;
    }

    /**
     * @param appSettings the appSettings to set
     */
    public void setAppSettings(AppSettings appSettings) {
        this.appSettings = appSettings;
    }

    /**
     * @return the shipName
     */
    public String getShipName() {
        return shipName;
    }

    /**
     * @param shipName the shipName to set
     */
    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    /**
     * @return the preGameModel
     */
    public GameModel getPreGameModel() {
        return preGameModel;
    }

}
