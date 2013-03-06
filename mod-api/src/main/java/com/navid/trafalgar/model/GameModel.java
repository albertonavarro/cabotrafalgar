package com.navid.trafalgar.model;

import java.util.*;

/**
 * This class represents a generic Game Model representation
 */
public class GameModel {

    private Map<Class, List<Object>> mapByClass = new HashMap();
    
    /**
     * Returns a collection with all interfaces and superclasses from an object
     * @param o Object
     * @return Class List
     */
    private List<Class> getSuperTypes(final Object o){
        List<Class> collection = new LinkedList<Class>();
        
        Class currentSuperClass = o.getClass();
        while(currentSuperClass != null){
            collection.add(currentSuperClass);
            collection.addAll(Arrays.asList(currentSuperClass.getInterfaces()));
            currentSuperClass = currentSuperClass.getSuperclass();
        }
        
        return collection;
    }

    /**
     * Adds an object to the model
     * @param o 
     */
    public void addToModel(Object o) {
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
     * Retrieves a List with all the registered objects that inherit from a class
     * @param className Class name
     * @return List of objects
     */
    public <T extends Object> List<T> getByType(Class<T> className) {
        List list = mapByClass.get(className);
        return list != null ? list : new ArrayList();
    }
    
    /**
     * Utility method to retrieve a single existing object of a type
     * 
     * @param className
     * @return requested object
     * @throws IllegalStateException if there a number of those objects different than one
     */
    public <T extends Object> T getSingleByType(Class<T>  className) {
        List list = getByType(className);
        
        if(list.size() != 1){
            throw new IllegalStateException("Required 1, found " + list.size() + " objects of type " + className);
        }
        
        return (T) list.get(0);
    }
    
}
