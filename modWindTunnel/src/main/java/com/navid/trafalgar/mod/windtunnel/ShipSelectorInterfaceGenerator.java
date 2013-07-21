/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.mod.windtunnel;

import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class ShipSelectorInterfaceGenerator implements ScreenGenerator {

    @Autowired
    private Nifty nifty;
    
    @Autowired
    private ScreenSelectShip screenSelectShip;
            
    @Override
    public void buildScreen() {
        nifty.fromXml("mod/windtunnel/interface_shipselector.xml", "selectShip", screenSelectShip);
    }

    /**
     * @param nifty the nifty to set
     */
    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }

    /**
     * @param screenSelectShip the screenSelectShip to set
     */
    public void setScreenSelectShip(ScreenSelectShip screenSelectShip) {
        this.screenSelectShip = screenSelectShip;
    }
    
    
    
}
