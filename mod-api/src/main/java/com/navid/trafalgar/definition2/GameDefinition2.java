package com.navid.trafalgar.definition2;

import java.util.List;

/**
 * Representation for a json2 file content
 * 
 */
public class GameDefinition2 {
    
    private List<Entry> entries;

    /**
     * @return the entries
     */
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * @param entries the entries to set
     */
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
    
}
