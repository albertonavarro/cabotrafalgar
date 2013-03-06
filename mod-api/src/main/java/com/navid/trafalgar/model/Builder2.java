package com.navid.trafalgar.model;

import com.jme3.scene.Node;
import com.navid.trafalgar.definition2.Entry;
import com.navid.trafalgar.definition2.GameDefinition2;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic Builder for all objects in the game
 */
public class Builder2 {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(Builder2.class);
    
    /**
     * Maps builder categories and builders
     */
    private Map<Category, Collection<BuilderInterface>> buildersByCategory =
            new EnumMap<Category, Collection<BuilderInterface>>(Category.class);
    /**
     * Maps builder names and builders
     */
    private Map<String, BuilderInterface> buildersByName = new HashMap<String, BuilderInterface>();

    /**
     * Categories (future use)
     */
    public enum Category {

        /**
         * Ship category in a palette (future use)
         */
        ship,
        /**
         * Item category in a palette (future use)
         */
        item,
        /**
         * Context category in a palette (future use)
         */
        context,
        /**
         * Other category in a palette (future use)
         */
        other
    }

    /**
     * Interactivity profile (future use)
     */
    public enum Interactivity {

        /**
         *
         */
        none,
        /**
         *
         */
        player,
        /**
         *
         */
        ghost
    }

    /**
     * Constructor
     */
    public Builder2() {
        buildersByCategory.put(Category.ship, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.context, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.item, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.other, new LinkedList<BuilderInterface>());
    }

    /**
     * Builds all the GameModel from a GameDefinition2 and a GameConfiguration objects
     * 
     * @param gameConfiguration
     * @param gameDef
     * @return
     */
    public GameModel build(GameConfiguration gameConfiguration, GameDefinition2 gameDef) {
        GameModel gameModel = new GameModel();

        for (Entry entry : gameDef.getEntries()) {
            if (entry.getName() != null && entry.getName().equals("player")) {
                entry.setType(gameConfiguration.getShipName());
            }

            Object o = build(entry);
            gameModel.addToModel(o);
        }

        for (Dependent currentDependant : (List<Dependent>) gameModel.getByType(Dependent.class)) {
            currentDependant.resolveDependencies(gameModel);
        }

        return gameModel;
    }

    /**
     * Registers a BuilderInterface builder in the Builder2, so it can be used by its name.
     *
     * @param builder
     */
    public void registerBuilder(BuilderInterface builder) {
        LOG.info("Registring builder %s", builder);

        for (Category currentCategory : builder.getCategories()) {
            buildersByCategory.get(currentCategory).add(builder);
        }

        if (buildersByName.get(builder.getType()) != null) {
            throw new IllegalStateException("Builder " + builder.getType() + " already exists");
        }

        buildersByName.put(builder.getType(), builder);
        LOG.info("Registring builder %s done", builder);
    }

    /**
     * Gets the collection of builders for a given category (future use)
     * 
     * @param category
     * @return
     */
    public Collection<BuilderInterface> getBuilder(Category category) {
        return buildersByCategory.get(category);
    }

    /**
     * Builds an object given some information in an Entry object
     *
     * @param entry
     * @return
     * @throws IllegalArgumentException if the object's type is not registered
     */
    public Object build(Entry entry) {
        if (buildersByName.get(entry.getType()) == null) {
            throw new IllegalArgumentException("Builder " + entry.getType() + " doesn't exist");
        }

        try{
            return buildersByName.get(entry.getType()).build(entry.getName(), entry.getValues());
        } catch (Exception e){
            return new Node();
        }
        
    }
}
