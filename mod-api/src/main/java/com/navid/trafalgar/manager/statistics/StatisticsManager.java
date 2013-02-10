package com.navid.trafalgar.manager.statistics;

import com.jme3.math.Vector3f;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alberto
 */
public class StatisticsManager {
    
    public static enum TYPES { FLOAT, VECTOR3F};
    
    private Map<String, Map<String, AbstractStatistic>> statsMap = new HashMap<String, Map<String, AbstractStatistic>>();
    
    public FloatStatistic createStatistic(String parentId, String statId, Float initValue){
        FloatStatistic result = new FloatStatistic(parentId, statId, initValue);
        registerStatistic(result);
        
        return result;
    }
    
    public Vector3fStatistic createStatistic(String parentId, String statId, Vector3f initValue){
        Vector3fStatistic result = new Vector3fStatistic(parentId, statId);
        result.setValue(initValue);
        
        registerStatistic(result);
        
        return result;
    }
    
    
    private  void registerStatistic(AbstractStatistic stats){
        getParentMap(stats.getParentId()).put(stats.getId(), stats);
    }
    
    public Collection<String> getParents(){
        return statsMap.keySet();
    }
    
    public Collection<String> getStatistics(String parentId){
        
        return getParentMap(parentId).keySet();
    }
    
    private Map<String, AbstractStatistic> getParentMap(String parentId){
        Map<String, AbstractStatistic> map = statsMap.get(parentId);
        
        if (map == null){
            map = new HashMap<String, AbstractStatistic>();
            statsMap.put(parentId, map);
        }
        
        return map;
    }
    
    public AbstractStatistic getStatistic(String parentId, String id){
        return statsMap.get(parentId).get(id);
    }
    
    public Map<String, AbstractStatistic> getAllStatistics(){
        Map<String, AbstractStatistic> result = new HashMap<String, AbstractStatistic>();
        
        for(String currentParent : getParents()){
            for(String currentId : getStatistics(currentParent)){
                result.put(currentId, getStatistic(currentParent, currentId));
            }
        }
        
        return result;
    }
    
}
