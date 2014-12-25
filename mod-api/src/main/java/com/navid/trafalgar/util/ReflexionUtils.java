package com.navid.trafalgar.util;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public final class ReflexionUtils {

    private ReflexionUtils() {
    }

    //TODO: REWRITE THIS SHIT
    public static Set<Class> getSuperTypes(Object o) {
        Set<Class> collection = new HashSet<Class>();
        Queue<Class> queue = new ArrayDeque();

        Class currentSuperClass = o.getClass();
        while (currentSuperClass != null) {
            Set<Class> innerCollection = new HashSet<Class>();
            innerCollection.add(currentSuperClass);
            innerCollection.addAll(Arrays.asList(currentSuperClass.getInterfaces()));
            innerCollection.add(currentSuperClass.getSuperclass());
            innerCollection.remove(null);
            innerCollection.removeAll(collection);
            queue.addAll(innerCollection);
            collection.addAll(innerCollection);
            currentSuperClass = queue.poll();
        }

        return collection;
    }

}
