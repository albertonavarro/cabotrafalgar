package com.navid.trafalgar.shipmodelz;

import com.navid.trafalgar.modapi.GenericModRegisterer;

public class InterfaceGenerator extends GenericModRegisterer {

    public InterfaceGenerator() {
        super(InterfaceGenerator.class.getResourceAsStream("mod-shipmodelz.yml"));
    }
}
