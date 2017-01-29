package com.navid.trafalgar.pantalan01.model;

import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.manager.EventManager;

import static com.google.common.collect.Lists.newArrayList;
import static com.navid.trafalgar.manager.EventManager.ILLEGAL_COLLISION;
import static com.navid.trafalgar.manager.EventManager.MILESTONE_REACHED;
import java.util.Collection;

public abstract class APantalan01Model extends Node implements Control {

    private EventManager eventManager;
    private boolean state = false;
    private Collection<? extends Node> collidables = newArrayList();

    public final void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public final void setCollidable(Collection<? extends Node> values) {
        this.collidables = values;
    }

    public final boolean getState() {
        return state;
    }

    @Override
    public final void update(float tpf) {


        CollisionResults results = new CollisionResults();
        for (Node currentCollidable : collidables) {
            this.collideWith(currentCollidable.getWorldBound(), results);

            if (!state && results.size() > 0) {
                state = true;
                eventManager.fireEvent(ILLEGAL_COLLISION);
            }
        }

    }

}
