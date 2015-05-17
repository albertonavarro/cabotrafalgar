package com.navid.trafalgar.maploader.v3;

import java.util.List;

/**
 * Representation for a map file content
 *
 */
public final class MapDefinition {

    private String picture;
    private String description;
    private List<EntryDefinition> entries;

    /**
     * @return the entries
     */
    public List<EntryDefinition> getEntries() {
        return entries;
    }

    /**
     * @param entries the entries to set
     */
    public void setEntries(List<EntryDefinition> entries) {
        this.entries = entries;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture the picture to set
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

}
