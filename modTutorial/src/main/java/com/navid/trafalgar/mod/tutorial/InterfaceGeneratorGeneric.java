package com.navid.trafalgar.mod.tutorial;

import com.navid.trafalgar.modapi.GenericModRegisterer;

public final class InterfaceGeneratorGeneric extends GenericModRegisterer {

    public InterfaceGeneratorGeneric() {
        super(InterfaceGeneratorGeneric.class.getResourceAsStream("tutorialmodconfig.yml"));
    }
}
