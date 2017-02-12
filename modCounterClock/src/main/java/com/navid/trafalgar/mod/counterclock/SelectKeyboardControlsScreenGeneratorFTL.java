package com.navid.trafalgar.mod.counterclock;

import de.lessvoid.nifty.Nifty;

import java.io.IOException;

public final class SelectKeyboardControlsScreenGeneratorFTL extends com.navid.trafalgar.mod.common.SelectKeyboardControlsScreenGeneratorFTL {

    public SelectKeyboardControlsScreenGeneratorFTL(Nifty nifty) throws IOException {
        super(nifty, "/mod/counterclock/interface_keyboardselector.xml");
    }

}
