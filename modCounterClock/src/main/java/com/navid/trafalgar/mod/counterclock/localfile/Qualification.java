package com.navid.trafalgar.mod.counterclock.localfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Qualification {

    private File fileName;

    private List<Float> times;

    private String shipClassName;

    public Qualification() {
        this.times = new ArrayList<Float>();

    }

    public Qualification(File fileName) {
        this.times = new ArrayList<Float>();
        this.fileName = fileName;
        calculateMapName(fileName);
    }

    /**
     * @return the times
     */
    public List<Float> getTimes() {
        return times;
    }

    /**
     * @param times the times to set
     */
    public void setTimes(List<Float> times) {
        this.times = times;
    }

    /**
     * @return the fileName
     */
    public File getFileName() {
        return fileName;
    }

    public File getMapName() {
        return calculateMapName(fileName);
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(File fileName) {
        this.fileName = fileName;
    }

    private File calculateMapName(File fileName) {
        return new File(fileName.getParent(), fileName.getName() + "_map");
    }

    /**
     * @return the shipClass
     */
    public String getShipClassName() {
        return shipClassName;
    }

    public Class getShipClass() {
        try {
            return Class.forName(shipClassName);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    /**
     * @param shipClass the shipClass to set
     */
    public void setShipClassName(String shipClass) {
        this.shipClassName = shipClass;
    }

    void setShipClass(Class stepRecordClass) {
        this.shipClassName = stepRecordClass.getName();
    }

}
