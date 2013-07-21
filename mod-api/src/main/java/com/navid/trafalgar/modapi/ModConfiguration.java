/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.modapi;

import java.util.List;

/**
 *
 * @author alberto
 */
public class ModConfiguration {
    
    private String modName;
    
    private String modPreGameSpringConfig;
    
    private String modGameSpringConfig;
    
    private String buildersSpringConfig;
    
    private List<ModScreenConfiguration> screenConfigurations;

    /**
     * @return the modName
     */
    public String getModName() {
        return modName;
    }

    /**
     * @param modName the modName to set
     */
    public void setModName(String modName) {
        this.modName = modName;
    }

    /**
     * @return the screenConfigurations
     */
    public List<ModScreenConfiguration> getScreenConfigurations() {
        return screenConfigurations;
    }

    /**
     * @param screenConfigurations the screenConfigurations to set
     */
    public void setScreenConfigurations(List<ModScreenConfiguration> screenConfigurations) {
        this.screenConfigurations = screenConfigurations;
    }

    /**
     * @return the modPreGameSpringConfig
     */
    public String getModPreGameSpringConfig() {
        return modPreGameSpringConfig;
    }

    /**
     * @param modPreGameSpringConfig the modPreGameSpringConfig to set
     */
    public void setModPreGameSpringConfig(String modPreGameSpringConfig) {
        this.modPreGameSpringConfig = modPreGameSpringConfig;
    }

    /**
     * @return the modGameSpringConfig
     */
    public String getModGameSpringConfig() {
        return modGameSpringConfig;
    }

    /**
     * @param modGameSpringConfig the modGameSpringConfig to set
     */
    public void setModGameSpringConfig(String modGameSpringConfig) {
        this.modGameSpringConfig = modGameSpringConfig;
    }

    /**
     * @return the buildersSpringConfig
     */
    public String getBuildersSpringConfig() {
        return buildersSpringConfig;
    }

    /**
     * @param buildersSpringConfig the buildersSpringConfig to set
     */
    public void setBuildersSpringConfig(String buildersSpringConfig) {
        this.buildersSpringConfig = buildersSpringConfig;
    }
    
    
    
}
