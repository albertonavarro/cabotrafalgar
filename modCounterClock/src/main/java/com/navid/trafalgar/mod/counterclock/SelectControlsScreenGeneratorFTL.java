package com.navid.trafalgar.mod.counterclock;

import de.lessvoid.nifty.Nifty;

import java.io.IOException;

/**
 * Created by anavarro on 12/02/17.
 */
public class SelectControlsScreenGeneratorFTL extends com.navid.trafalgar.mod.common.SelectControlsScreenGeneratorFTL {
    public SelectControlsScreenGeneratorFTL(Nifty nifty) throws IOException {
        super(nifty, "/mod/counterclock/interface_selectcontrols.xml");
    }
}
