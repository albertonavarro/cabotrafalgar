package com.navid.trafalgar.maploader.v3;

import java.util.HashMap;
import java.util.Map;

/**
 * Representation for a map entry
 *
 */
public final class EntryDefinition {

    /*
     * Classname for the entry
     */
    private String type;

    /*
     * Id for the object
     */
    private String name;

    /*
     * Custom parameters
     */
    private Map<String, Object> values = new HashMap();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the values
     */
    public Map<String, Object> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
