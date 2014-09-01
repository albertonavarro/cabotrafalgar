package com.navid.trafalgar.model;

import com.navid.trafalgar.util.ReflexionUtils;
import java.util.*;

/**
 *
 *
 */
public class GameModel implements GameModelInterface {

    private Map<Class, List<Object>> mapByClass = new HashMap();

    @Override
    public void addToModel(Collection collection) {

        for (Object o : collection) {
            for (Class currentClass : ReflexionUtils.getSuperTypes(o)) {
                List list = mapByClass.get(currentClass);
                if (list == null) {
                    list = new ArrayList();
                    mapByClass.put(currentClass, list);
                }
                list.add(o);
            }
        }
    }

    /**
     *
     * @param <T>
     * @param className
     * @return
     */
    @Override
    public <T> List<T> getByType(Class<T> className) {
        List list = mapByClass.get(className);
        return list != null ? list : new ArrayList();
    }

    /**
     *
     * @param <T>
     * @param className
     * @return
     */
    @Override
    public <T> T getSingleByType(Class<T> className) {
        List list = getByType(className);

        if (list.size() != 1) {
            throw new IllegalStateException("Required 1, found " + list.size() + " objects of type " + className);
        }

        return (T) list.get(0);
    }

    @Override
    public void removeFromModel(Class className) {
        List list = getByType(className);
        
        for(Object o : list){
            mapByClass.remove(o.getClass());
        }
    }

    public boolean contains(Class className) {
        return ! getByType(className).isEmpty();
    }
}
