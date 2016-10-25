package com.navid.trafalgar.mod.counterclock;

import com.navid.nifty.flow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.annotation.Autowired;

public final class SelectProfileScreenGenerator implements ScreenGenerator {

    @Autowired
    private Nifty nifty;

    @Override
    public void buildScreen(String screenUniqueId, String controllerClass) {
        if (nifty.getScreen(screenUniqueId) == null) {
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
