package com.navid.trafalgar.mod.counterclock.statelisteners;

import au.com.bytecode.opencsv.CSVWriter;
import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.manager.statistics.AbstractStatistic;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CSVRecorder implements PrestartState, StartedState {

    @Autowired
    private StatisticsManager statsManager;
    private Map<String, AbstractStatistic> mapstats;
    private CSVWriter writer;

    private String[] getHeaders() {
        String[] newLine = new String[mapstats.size()];
        List<String> listNames = new ArrayList(mapstats.keySet());
        for (int index = 0; index < mapstats.size(); index++) {
            newLine[index] = listNames.get(index);
        }

        return newLine;
    }

    private String[] getValues() {
        String[] newLine = new String[mapstats.size()];
        List<String> listNames = new ArrayList(mapstats.keySet());
        for (int index = 0; index < mapstats.size(); index++) {
            newLine[index] = mapstats.get(listNames.get(index)).getValue().toString();
        }

        return newLine;
    }

    @Override
    public void onPrestart(float tpf) {
        mapstats = statsManager.getAllStatistics();

        try {
            writer = new CSVWriter(new FileWriter("windtunnel" + System.currentTimeMillis() + ".csv"));

            writer.writeNext(getHeaders());
        } catch (IOException ex) {
            Logger.getLogger(CSVRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onUnload() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(CSVRecorder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void onStarted(float tpf) {
        if (writer != null) {
            writer.writeNext(getValues());
        }
    }

    /**
     * @param statsManager the statsManager to set
     */
    public void setStatsManager(StatisticsManager statsManager) {
        this.statsManager = statsManager;
    }
}
