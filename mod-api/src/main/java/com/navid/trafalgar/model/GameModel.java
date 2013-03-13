package com.navid.trafalgar.model;

import java.util.*;

/**
 *
 *   
 */
public class GameModel {

    private Map<Class, List<Object>> mapByClass = new HashMap();
    
    private Iterable<Class> getSuperTypes(Object o){
        List<Class> collection = new LinkedList<Class>();
        
        Class currentSuperClass = o.getClass();
        while(currentSuperClass != null){
            collection.add(currentSuperClass);
            collection.addAll(Arrays.asList(currentSuperClass.getInterfaces()));
            currentSuperClass = currentSuperClass.getSuperclass();
        }
        
        return collection;
    }

    void addToModel(Object o) {
        for (Class currentClass : getSuperTypes(o) ) {
            List list = mapByClass.get(currentClass);
            if (list == null) {
                list = new ArrayList();
                mapByClass.put(currentClass, list);
            }
            list.add(o);
        }
    }

    /**
     * 
     * @param <T>
     * @param className
     * @return 
     */
    public <T>  List<T> getByType(Class<T> className) {
        List list = mapByClass.get(className);
        return list != null ? list : new ArrayList();
    }
    
    /**
     * 
     * @param <T>
     * @param className
     * @return 
     */
    public <T> T getSingleByType(Class<T> className) {
        List list = getByType(className);
        
        if(list.size() != 1){
            throw new IllegalStateException("Required 1, found " + list.size() + " objects of type " + className);
        }
        
        return (T) list.get(0);
    }
    
}
