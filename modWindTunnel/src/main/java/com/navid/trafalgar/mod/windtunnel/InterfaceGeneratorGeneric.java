package com.navid.trafalgar.mod.windtunnel;

import com.navid.trafalgar.modapi.GenericModRegisterer;

public final class InterfaceGeneratorGeneric extends GenericModRegisterer {

    public InterfaceGeneratorGeneric() {
        super(InterfaceGeneratorGeneric.class.getResourceAsStream("windtunnelmodconfig.yml"));
    }

}
