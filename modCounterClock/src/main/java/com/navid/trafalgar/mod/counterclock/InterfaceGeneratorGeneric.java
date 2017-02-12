package com.navid.trafalgar.mod.counterclock;

import com.navid.trafalgar.modapi.GenericModRegisterer;

public final class InterfaceGeneratorGeneric extends GenericModRegisterer {

    public InterfaceGeneratorGeneric() {
        super(InterfaceGeneratorGeneric.class.getResourceAsStream("counterclockmodconfig.yml"));
    }


}
