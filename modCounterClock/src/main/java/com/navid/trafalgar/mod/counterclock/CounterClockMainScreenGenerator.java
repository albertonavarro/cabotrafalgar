/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.mod.counterclock;

import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class CounterClockMainScreenGenerator implements ScreenGenerator {

    @Autowired
    private Nifty nifty;
            
    @Override
    public void buildScreen() {
        nifty.addXml("mod/counterclock/interface_counterclock.xml");
    }

    /**
     * @param nifty the nifty to set
     */
    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }
    
}