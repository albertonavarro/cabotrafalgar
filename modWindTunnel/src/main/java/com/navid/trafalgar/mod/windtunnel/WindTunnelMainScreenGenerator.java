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
public class WindTunnelMainScreenGenerator implements ScreenGenerator {

    @Autowired
    private Nifty nifty;
    
    @Autowired
    private WindTunnelMainScreen screenSelectShip;
            
    @Override
    public void buildScreen() {
        nifty.addXml("mod/windtunnel/interface_windtunnel.xml");
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
    public void setScreenSelectShip(WindTunnelMainScreen screenSelectShip) {
        this.screenSelectShip = screenSelectShip;
    }
    
    
    
}