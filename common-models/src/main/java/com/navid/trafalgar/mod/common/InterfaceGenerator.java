package com.navid.trafalgar.mod.common;

import com.navid.trafalgar.modapi.GenericModRegisterer;

/**
 *
 * @author alberto
 */
public class InterfaceGenerator extends GenericModRegisterer{
    
    public InterfaceGenerator(){
        super(InterfaceGenerator.class.getResourceAsStream("com.navid.trafalgar.mod.common/commonmodconfig.yml"));
    }
}
