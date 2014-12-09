/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.modapi;

import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author alberto
 */
public class ModScreenConfiguration {

    private String screenName;
    private String interfaceConstructor;
    private String controller;

    /**
     * @return the screenName
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * @param screenName the screenName to set
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    /**
     * @return the interfaceConstructor
     */
    public String getInterfaceConstructor() {
        return interfaceConstructor;
    }

    /**
     * @param interfaceConstructor the interfaceConstructor to set
     */
    public void setInterfaceConstructor(String interfaceConstructor) {
        this.interfaceConstructor = interfaceConstructor;
    }

    /**
     * @return the controller
     */
    public String getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(String controller) {
        this.controller = controller;
    }

}
