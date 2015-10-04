package com.navid.trafalgar.shipmodely;

import com.navid.trafalgar.audio.MusicManager;
import com.navid.trafalgar.modapi.GenericModRegisterer;

public class InterfaceGenerator extends GenericModRegisterer {

    public InterfaceGenerator() {
        super(InterfaceGenerator.class.getResourceAsStream("mod-shipmodely.yml"));
    }

}
