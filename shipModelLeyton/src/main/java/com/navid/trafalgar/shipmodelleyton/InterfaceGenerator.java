package com.navid.trafalgar.shipmodelleyton;

import com.navid.trafalgar.audio.MusicManager;
import com.navid.trafalgar.modapi.GenericModRegisterer;

public class InterfaceGenerator extends GenericModRegisterer {

    public InterfaceGenerator() {
        super(InterfaceGenerator.class.getResourceAsStream("mod-shipmodelleyton.yml"));
    }

}
