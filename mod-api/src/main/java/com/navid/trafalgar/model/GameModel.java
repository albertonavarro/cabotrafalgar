package com.navid.trafalgar.model;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.navid.trafalgar.util.ReflexionUtils;
import java.util.*;

import static com.google.common.collect.Sets.newHashSet;

/**
 *
 *
 */
public final class GameModel implements GameModelInterface {

    private final Map<Class, Set<Object>> mapByClass = new HashMap();
    private final Map<String, Set<Object>> mapByName = new HashMap();

    @Override
    public void addToModel(Collection collection) {

        for (Object o : collection) {
            for (Class currentClass : ReflexionUtils.getSuperTypes(o)) {
                Set list = mapByClass.get(currentClass);
                if (list == null) {
                    list = new HashSet();
                    mapByClass.put(currentClass, list);
                }
                list.add(o);
            }
        }
    }

    @Override
    public void addToModel(Collection collection, String name) {

        addToModel(collection);


        for (Object o : collection) {
            for (Class currentClass : ReflexionUtils.getSuperTypes(o)) {
                Set list = mapByName.get(name);
                if (list == null) {
                    list = new HashSet();
                    mapByName.put(name, list);
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
        Set list = mapByClass.get(className);
        return list != null ? new ArrayList<T>(list) : new ArrayList();
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
    public <T> T getSingleByTypeAndName(Class<T> tClass, String name) {
        Set listClass = mapByClass.get(tClass);
        listClass = listClass == null? newHashSet() : listClass;

        Set listName = mapByName.get(name);
        listName = listName == null? newHashSet() : listName;

        Set result = Sets.intersection(listClass, listName);

        if (result.size() != 1) {
            throw new IllegalStateException("Required 1, found " + result.size() + " objects of type " + tClass + " and name " + name);
        }

        return (T) Iterables.getFirst(result, null);
    }

    @Override
    public void removeFromModel(Class className) {
        List list = getByType(className);

        for (Object o : list) {
            for (Class currentClass : ReflexionUtils.getSuperTypes(o)) {
                mapByClass.remove(currentClass);
            }
        }
    }

    public boolean contains(Class className) {
        return !getByType(className).isEmpty();
    }


    public Collection getAll() {
        return mapByClass.get(Object.class);
    }
}
