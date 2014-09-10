/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.navid.trafalgar.mod.counterclock;

import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author casa
 */
public class SelectProfileScreenGenerator implements ScreenGenerator {
    @Autowired
    private Nifty nifty;
         
    @Override
    public void buildScreen() {
        if(nifty.getScreen("selectProfile") == null){
            nifty.addXml("mod/counterclock/interface_profileselector.xml");
        }
    }

    /**
     * @param nifty the nifty to set
     */
    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }
}
