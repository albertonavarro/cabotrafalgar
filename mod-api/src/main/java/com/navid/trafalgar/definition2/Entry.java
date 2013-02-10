package com.navid.trafalgar.definition2;

import java.util.Map;

/**
 *
 * @author alberto
 */
public class Entry {
    
    private String type;
    
    private String name;
    
    private Map<String, String> values;

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
    public Map<String, String> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(Map<String, String> values) {
        this.setValues(values);
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
