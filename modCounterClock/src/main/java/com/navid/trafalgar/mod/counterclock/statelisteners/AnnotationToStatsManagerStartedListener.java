/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.manager.statistics.AbstractStatistic;
import com.navid.trafalgar.manager.statistics.Auditable;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.counterclock.CounterClockGameModel;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.util.ReflexionUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class AnnotationToStatsManagerStartedListener implements PrestartState, StartedState {
    
    
    @Autowired
    private CounterClockGameModel gameModel;
    
    @Autowired
    private StatisticsManager statsManager;
    
    private AShipModel ship;
    
    private Map<Field, AbstractStatistic> fields = new HashMap<Field,AbstractStatistic>();

    @Override
    public void onPrestart(float tpf) {
        ship = gameModel.getShip();
        
        Iterable<Class> classes = ReflexionUtils.getSuperTypes(ship);
        for(Class currentClass : classes){
            for(Field currentField : currentClass.getDeclaredFields()){
                for(Annotation currentAnnotation : currentField.getAnnotations()){
                    if(currentAnnotation.annotationType().equals(Auditable.class)){
                        if(!currentField.isAccessible()){
                            currentField.setAccessible(true);
                        }
                       
                        fields.put(currentField, statsManager.createStatistic("shipZeroStats", currentField.getName(), 0f));
                    }
                }
            }
        }
    }

    @Override
    public void onUnload() {

    }

    @Override
    public void onStarted(float tpf) {
        long timeA = System.currentTimeMillis();
        for (Map.Entry<Field, AbstractStatistic> field : fields.entrySet()){
            try {
                Object value = field.getKey().get(ship);
                field.getValue().setValue(value);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(AnnotationToStatsManagerStartedListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(AnnotationToStatsManagerStartedListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Time evaluating internal values: " + (System.currentTimeMillis() - timeA));
        
    }

    /**
     * @param model the model to set
     */
    public void setModel(CounterClockGameModel model) {
        this.setGameModel(model);
    }

    /**
     * @param gameModel the gameModel to set
     */
    public void setGameModel(CounterClockGameModel gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * @param statsManager the statsManager to set
     */
    public void setStatsManager(StatisticsManager statsManager) {
        this.statsManager = statsManager;
    }
    
    
    
}
