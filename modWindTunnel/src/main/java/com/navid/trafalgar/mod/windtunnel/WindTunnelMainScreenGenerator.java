package com.navid.trafalgar.mod.windtunnel;

import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.annotation.Autowired;

public class WindTunnelMainScreenGenerator implements ScreenGenerator {

    @Autowired
    private Nifty nifty;

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
}
