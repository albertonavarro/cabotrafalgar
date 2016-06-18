package com.navid.trafalgar.model;

import com.navid.trafalgar.maploader.v3.EntryDefinition;
import com.navid.trafalgar.maploader.v3.MapDefinition;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModelBuilder {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(ModelBuilder.class);
    /**
     * Maps builder categories and builders
     */
    private final Map<Category, Collection<BuilderInterface>> buildersByCategory
            = new EnumMap<Category, Collection<BuilderInterface>>(Category.class);
    /**
     * Maps builder names and builders
     */
    private final Map<String, BuilderInterface> buildersByName = new HashMap<String, BuilderInterface>();

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
    public ModelBuilder() {
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
    public GameModel build(final GameConfiguration gameConfiguration, MapDefinition gameDef) {
        GameModel gameModel = new GameModel();

        for (EntryDefinition entry : gameDef.getEntries()) {
            if (entry.getName() != null && entry.getName().equals("player")) {
                //entry.setType(gameConfiguration.getShipName());
                //gameModel.addToModel(gameConfiguration.getPreGameModel().getByType(AShipModel.class));
                EntryDefinition entryInput = new EntryDefinition();
                entryInput.setType(gameConfiguration.getShipName());
                entryInput.setName("player1");
                entryInput.setValues(new HashMap<String, Object>() {
                    {
                        put("role", "Player");
                    }
                });
                Collection c = build(entryInput);
                gameModel.addToModel(c);
            } else {
                gameModel.addToModel(build(entry));
            }
        }

        for (Dependent currentDependant : (List<Dependent>) gameModel.getByType(Dependent.class)) {
            currentDependant.resolveDependencies(gameModel);
        }

        for (Dependent currentDependant : (List<Dependent>) gameModel.getByType(Dependent.class)) {
            currentDependant.commitDependencies();
        }

        return gameModel;
    }

    /**
     *
     * @param builder
     */
    public void registerBuilder(BuilderInterface builder) {
        LOG.info("Registring builder " + builder);

        if (buildersByName.containsKey(builder.getType())) {
            throw new IllegalStateException("Builder " + builder.getType() + " already exists");
        }
        for (Category currentCategory : builder.getCategories()) {
            buildersByCategory.get(currentCategory).add(builder);
        }

        buildersByName.put(builder.getType(), builder);
        LOG.info("Registring builder " + builder + " done");
    }

    /**
     * Get builder by category
     * @param category
     * @return
     */
    public Collection<BuilderInterface> getBuilder(Category category) {
        return buildersByCategory.get(category);
    }

    /**
     * Get builder by name
     * @param builderName
     * @return
     */
    public BuilderInterface getBuilder(String builderName) {
        return buildersByName.get(builderName);
    }

    /**
     *
     * @param entry
     * @return
     */
    public Collection build(EntryDefinition entry) {
        if (buildersByName.get(entry.getType()) == null) {
            throw new IllegalArgumentException("Builder " + entry.getType() + " doesn't exist");
        }

        return buildersByName.get(entry.getType()).build(entry.getName(), entry.getValues());
    }

    /**
     *
     * @param entry
     * @param gameModel
     * @return
     */
    public Collection buildWithDependencies(EntryDefinition entry, GameModel gameModel) {
        if (buildersByName.get(entry.getType()) == null) {
            throw new IllegalArgumentException("Builder " + entry.getType() + " doesn't exist");
        }

        Collection objects = buildersByName.get(entry.getType()).build(entry.getName(), entry.getValues());

        for (Object o : objects) {
            if (o instanceof Dependent) {
                ((Dependent) o).resolveDependencies(gameModel);
            }
        }

        return objects;
    }
}
