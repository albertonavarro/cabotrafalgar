package com.navid.trafalgar.mod.common;

import com.navid.trafalgar.modapi.GenericModRegisterer;

/**
 *
 * @author alberto
 */
public final class InterfaceGenerator extends GenericModRegisterer {

    public InterfaceGenerator() {
        super(InterfaceGenerator.class.getResourceAsStream("commonmodconfig.yml"));
    }
}
