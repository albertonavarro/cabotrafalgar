package com.navid.trafalgar.pantalan01.model;

import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.AShipModelPlayer;
import com.navid.trafalgar.model.Dependent;
import com.navid.trafalgar.model.GameModel;

import static com.google.common.collect.Lists.newArrayList;
import java.util.Collection;

public abstract class APantalan01Model extends Node implements Control, Dependent {

    private EventManager eventManager;
    private Collection<? extends Node> collidables = newArrayList();

    @Override
    public final void update(float tpf) {


        CollisionResults results = new CollisionResults();
        for (Node currentCollidable : collidables) {
            this.collideWith(currentCollidable.getWorldBound(), results);

            if (results.size() > 0) {
                eventManager.fireEvent(EventManager.FAILED);
            }
        }

    }

    @Override
    public void resolveDependencies(GameModel gameModel) {
        collidables = newArrayList((Node)gameModel.getSingleByTypeAndName(AShipModelPlayer.class, "player1"));
        collidables.remove(this);

        eventManager = gameModel.getSingleByType(EventManager.class);
    }

    @Override
    public void commitDependencies() {

    }

}
