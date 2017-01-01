package com.navid.trafalgar.model;

import com.jme3.scene.Geometry;
import com.navid.trafalgar.maploader.v3.EntryDefinition;
import com.navid.trafalgar.maploader.v3.MapDefinition;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Collections.emptyList;

public final class ModelBuilder {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ModelBuilder.class);
    /**
     * Maps builder categories and builders
     */
    private final Map<Category, Collection<BuilderInterface>> buildersByCategory
            = new EnumMap<Category, Collection<BuilderInterface>>(Category.class);
    /**
     * Maps builder names and builders
     */
    private final Map<String, BuilderInterface> buildersByName = new HashMap<>();

    /**
     * Categories (future use)
     */
    public enum Category {ship, item, context, other}

    /**
     * Interactivity profile (future use)
     */
    public enum Interactivity { none, player, ghost}

    public ModelBuilder() {
        buildersByCategory.put(Category.ship, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.context, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.item, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.other, new LinkedList<BuilderInterface>());
    }

    public GameModel buildControls(final GameConfiguration gameConfiguration, MapDefinition gameDef) {
        GameModel gameModel = new GameModel();

        fillGameModel(gameConfiguration, gameDef, Role.controls, gameModel);

        return gameModel;
    }

    public GameModel buildGeometry(final GameConfiguration gameConfiguration, MapDefinition gameDef) {
        GameModel gameModel = new GameModel();

        gameModel.addToModel(gameConfiguration.getControls().getAll());
        gameModel.addToModel(gameConfiguration.getCustom().getAll());

        fillGameModel(gameConfiguration, gameDef, Role.geometry, gameModel);

        for (Dependent currentDependant : gameModel.getByType(Dependent.class)) {
            currentDependant.resolveDependencies(gameModel);
        }

        for (Dependent currentDependant : gameModel.getByType(Dependent.class)) {
            currentDependant.commitDependencies();
        }

        return gameModel;
    }

    public GameModel build(final GameConfiguration gameConfiguration, MapDefinition gameDef, final Role role) {
        return build(null, gameConfiguration, gameDef, role);
    }

    public GameModel build(GameModel previousModels, final GameConfiguration gameConfiguration, MapDefinition gameDef, final Role role) {
        GameModel gameModel = new GameModel();

        if(previousModels!=null) {
            gameModel.addToModel(previousModels.getAll());
        }

        fillGameModel(gameConfiguration, gameDef, role, gameModel);

        for (Dependent currentDependant : gameModel.getByType(Dependent.class)) {
            currentDependant.resolveDependencies(gameModel);
        }

        for (Dependent currentDependant : gameModel.getByType(Dependent.class)) {
            currentDependant.commitDependencies();
        }

        return gameModel;
    }

    private void fillGameModel(GameConfiguration gameConfiguration, MapDefinition gameDef, final Role role, GameModel gameModel) {
        for (EntryDefinition entry : gameDef.getEntries()) {
            if (entry.getName() != null && entry.getName().equals("player")) {
                //entry.setType(gameConfiguration.getShipName());
                //gameModel.addToModel(gameConfiguration.getPreGameModel().getByType(AShipModel.class));
                EntryDefinition entryInput = new EntryDefinition();
                entryInput.setType(gameConfiguration.getShipName());
                entryInput.setName("player1");
                entryInput.setValues(new HashMap<String, Object>() {
                    {
                        put("role", role);
                    }
                });
                Collection c = build(entryInput, role);
                gameModel.addToModel(c, "player1");
            } else {
                entry.getValues().put("role", role);
                gameModel.addToModel(build(entry, role));
            }
        }
    }

    /**
     *
     * @param builder
     */
    public void registerBuilder(BuilderInterface builder) {
        logger.info("Registring builder " + builder);

        if (buildersByName.containsKey(builder.getType())) {
            throw new IllegalStateException("Builder " + builder.getType() + " already exists");
        }
        for (Category currentCategory : builder.getCategories()) {
            buildersByCategory.get(currentCategory).add(builder);
        }

        buildersByName.put(builder.getType(), builder);
        logger.info("Registring builder " + builder + " done");
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
    public Collection build(EntryDefinition entry, Role role) {
        if (buildersByName.get(entry.getType()) == null) {
            throw new IllegalArgumentException("Builder " + entry.getType() + " doesn't exist");
        }

        BuilderInterface builder = buildersByName.get(entry.getType());
        switch (role) {
            case controls:
                return builder.buildControls(entry.getName(), entry.getValues());
            case geometry:
                return builder.buildGeometry(entry.getName(), entry.getValues());
            case ghost:
                return builder.buildGhost(entry.getName(), entry.getValues());
            case candidateRecord:
                return builder.buildCandidateRecord(entry.getName(), entry.getValues());
            default: throw new IllegalArgumentException(role.name());
        }
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

        Collection objects = buildersByName.get(entry.getType()).buildGeometry(entry.getName(), entry.getValues());

        for (Object o : objects) {
            if (o instanceof Dependent) {
                ((Dependent) o).resolveDependencies(gameModel);
            }
        }

        for (Object o : objects) {
            if (o instanceof Dependent) {
                ((Dependent) o).commitDependencies();
            }
        }

        return objects;
    }
}
