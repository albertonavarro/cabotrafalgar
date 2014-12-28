package com.navid.trafalgar.manager.statistics;

import com.jme3.math.Vector3f;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * StatisticsManager generates, stores and indexes different implementations for
 * AbstactStatistic.
 *
 * An AbstractStatistic is going to allow us to hold a value, and at the same
 * time, share it with other listeners in order to track, show or process it.
 *
 * Examples for AbstractStatistics could be Speed, Time, Direction of the ship..
 *
 * Moreover, they could have their own calculus for Average, Peak..
 *
 * @see AbstractStatistic
 */
public final class StatisticsManager {

    /**
     *
     */
    public static enum TYPES {

        /**
         *
         */
        FLOAT,
        /**
         *
         */
        VECTOR3F
    };

    private final Map<String, Map<String, AbstractStatistic>> statsMap = new HashMap<String, Map<String, AbstractStatistic>>();

    /**
     * Creates float statistic
     *
     * @param parentId ParentId for this statistic
     * @param statId Id for this statistic
     * @param initValue Initial value
     * @return already registered FloatStatistic
     */
    public FloatStatistic createStatistic(String parentId, String statId, Float initValue) {
        FloatStatistic result = new FloatStatistic(parentId, statId, initValue);
        registerStatistic(result);

        return result;
    }

    /**
     * Creates vector statistic
     *
     * @param parentId
     * @param statId
     * @param initValue
     * @return
     */
    public Vector3fStatistic createStatistic(String parentId, String statId, Vector3f initValue) {
        Vector3fStatistic result = new Vector3fStatistic(parentId, statId);
        result.setValue(initValue);

        registerStatistic(result);

        return result;
    }

    private void registerStatistic(AbstractStatistic stats) {
        getParentMap(stats.getParentId()).put(stats.getId(), stats);
    }

    /**
     *
     * @return
     */
    public Collection<String> getParents() {
        return statsMap.keySet();
    }

    /**
     *
     * @param parentId
     * @return
     */
    public Collection<String> getStatistics(String parentId) {

        return getParentMap(parentId).keySet();
    }

    private Map<String, AbstractStatistic> getParentMap(String parentId) {
        Map<String, AbstractStatistic> map = statsMap.get(parentId);

        if (map == null) {
            map = new HashMap<String, AbstractStatistic>();
            statsMap.put(parentId, map);
        }

        return map;
    }

    /**
     *
     * @param parentId
     * @param id
     * @return
     */
    public AbstractStatistic getStatistic(String parentId, String id) {
        return statsMap.get(parentId).get(id);
    }

    /**
     *
     * @return
     */
    public Map<String, AbstractStatistic> getAllStatistics() {
        Map<String, AbstractStatistic> result = new HashMap<String, AbstractStatistic>();

        for (String currentParent : getParents()) {
            for (String currentId : getStatistics(currentParent)) {
                result.put(currentId, getStatistic(currentParent, currentId));
            }
        }

        return result;
    }

}
