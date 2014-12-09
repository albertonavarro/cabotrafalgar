package com.navid.trafalgar.shipmodely;

import com.navid.trafalgar.modapi.GenericModRegisterer;

/**
 *
 * @author alberto
 */
public class InterfaceGenerator extends GenericModRegisterer {

    public InterfaceGenerator() {
        super(InterfaceGenerator.class.getResourceAsStream("mod-shipmodely.yml"));
    }
}
