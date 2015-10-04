package com.navid.trafalgar.mod.common;

import com.navid.trafalgar.audio.MusicManager;
import com.navid.trafalgar.modapi.GenericModRegisterer;

public final class InterfaceGenerator extends GenericModRegisterer {

    public InterfaceGenerator() {
        super(InterfaceGenerator.class.getResourceAsStream("commonmodconfig.yml"));
    }


}
