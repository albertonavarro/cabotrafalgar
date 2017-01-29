package com.navid.trafalgar.pantalan01;

import com.navid.trafalgar.audio.MusicManager;
import com.navid.trafalgar.modapi.GenericModRegisterer;

public class InterfaceGenerator extends GenericModRegisterer {

    public InterfaceGenerator() {
        super(InterfaceGenerator.class.getResourceAsStream("mod-pantalan01.yml"));
    }

}
