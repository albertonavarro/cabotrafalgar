package com.navid.trafalgar.model;

import com.jme3.system.AppSettings;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 *  
 */
public class GameConfiguration {

    /**
     * @return the showGhost
     */
    public ShowGhost getShowGhost() {
        return showGhost;
    }
    
    public static enum ShowGhost {noghost, bestLocal, bestRemote}

    
    private String map;
    
    private ShowGhost showGhost;
    
    @Autowired
    private AppSettings appSettings;
    
    private String shipName;
    
    private GameModel preGameModel;
    
    public GameConfiguration(){
        preGameModel = new GameModel();
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
     * @param showGhost the showGhost to set
     */
    public void setShowGhost(ShowGhost showGhost) {
        this.showGhost = showGhost;
    }
    
    

    /**
     *
     * @param gc
     */
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
        
        if(gc.getShowGhost() != null){
            this.setShowGhost(gc.getShowGhost());
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
