package com.navid.trafalgar.model;

public final class Header {

    private static final int VERSION = 1;

    private String map;

    private String shipModel;

    /**
     * @return the version
     */
    public int getVersion() {
        return VERSION;
    }

    public void setVersion(int newVersion) {
        if (newVersion != VERSION) {
            throw new RuntimeException("Different version!");
        }
    }

    /**
     * @return the map
     */
    public String getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(String map) {
        this.map = map;
    }

    /**
     * @return the shipModel
     */
    public String getShipModel() {
        return shipModel;
    }

    /**
     * @param shipModel the shipModel to set
     */
    public void setShipModel(String shipModel) {
        this.shipModel = shipModel;
    }

}
