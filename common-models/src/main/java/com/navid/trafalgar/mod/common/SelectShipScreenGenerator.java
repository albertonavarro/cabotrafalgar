package com.navid.trafalgar.mod.common;

import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.annotation.Autowired;

public final class SelectShipScreenGenerator implements ScreenGenerator {

    @Autowired
    private Nifty nifty;

    @Override
    public void buildScreen() {
        if (nifty.getScreen("selectShip") == null) {
            nifty.addXml("mod/common/interface_shipselector.xml");
        }
    }

    /**
     * @param nifty the nifty to set
     */
    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }
}
