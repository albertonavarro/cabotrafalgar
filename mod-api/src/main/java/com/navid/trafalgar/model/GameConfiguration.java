package com.navid.trafalgar.model;

import com.jme3.system.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author anf
 */
public class GameConfiguration {
    
    private String map;
    
    private Boolean showGhost;
    
    @Autowired
    private AppSettings appSettings;
    
    private String shipName;
    

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
     * @return the showGhost
     */
    public Boolean isShowGhost() {
        return showGhost;
    }

    /**
     * @param showGhost the showGhost to set
     */
    public void setShowGhost(Boolean showGhost) {
        this.showGhost = showGhost;
    }

    public void fromGameConfiguration(GameConfiguration gc) {
        if(gc.getMap()!=null){
            this.setMap(gc.getMap());
        }
        
        if(gc.getAppSettings()!= null){
            this.setAppSettings(gc.getAppSettings());
        }
        
        if(gc.getShipName() != null){
            this.setShipName( gc.getShipName());
        }
        
        if(gc.isShowGhost() != null){
            this.setShowGhost(gc.isShowGhost());
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

    
    
    
    
}
