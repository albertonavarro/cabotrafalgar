/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author alberto
 */
public class ReflexionUtils {
    
    private ReflexionUtils(){
    }
    
    public static Iterable<Class> getSuperTypes(Object o){
        List<Class> collection = new LinkedList<Class>();
        
        Class currentSuperClass = o.getClass();
        while(currentSuperClass != null){
            collection.add(currentSuperClass);
            collection.addAll(Arrays.asList(currentSuperClass.getInterfaces()));
            currentSuperClass = currentSuperClass.getSuperclass();
        }
        
        return collection;
    }
    
}
