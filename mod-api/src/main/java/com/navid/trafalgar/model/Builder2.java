package com.navid.trafalgar.model;

import com.jme3.scene.Node;
import com.navid.trafalgar.definition2.Entry;
import com.navid.trafalgar.definition2.GameDefinition2;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
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
         *
         */
        ship,
        /**
         *
         */
        item,
        /**
         *
         */
        context,
        /**
         *
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
     *
     */
    public Builder2() {
        buildersByCategory.put(Category.ship, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.context, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.item, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.other, new LinkedList<BuilderInterface>());
    }

    /**
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
                gameModel.addToModel(gameConfiguration.getPreGameModel().getByType(AShipModel.class));
            }else{
                gameModel.addToModel(build(entry));
            }

            
        }
        

        for (Dependent currentDependant : (List<Dependent>) gameModel.getByType(Dependent.class)) {
            currentDependant.resolveDependencies(gameModel);
        }

        return gameModel;
    }

    /**
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
     *
     * @param category
     * @return
     */
    public Collection<BuilderInterface> getBuilder(Category category) {
        return buildersByCategory.get(category);
    }

    /**
     *
     * @param entry
     * @return
     */
    public Collection build(Entry entry) {
        if (buildersByName.get(entry.getType()) == null) {
            throw new IllegalArgumentException("Builder " + entry.getType() + " doesn't exist");
        }

        return buildersByName.get(entry.getType()).build(entry.getName(), entry.getValues());
    }
}
