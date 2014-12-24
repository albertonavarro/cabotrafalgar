package com.navid.trafalgar.mod.counterclock.model;

import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.manager.EventManager;
import static com.navid.trafalgar.manager.EventManager.MILLESTONE_REACHED;
import java.util.Collection;

public abstract class AMillestoneModel extends Node implements Control {

    private EventManager eventManager;
    private boolean state = false;
    private Collection<? extends Node> collidables;
    private Material materialOn;
    private Material materialOff;

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
        if (state) {
            this.setMaterial(materialOn);
        } else {
            this.setMaterial(materialOff);
        }

        CollisionResults results = new CollisionResults();
        for (Node currentCollidable : collidables) {
            currentCollidable.collideWith(this.getWorldBound(), results);

            if (!state && results.size() > 0) {
                state = true;
                eventManager.fireEvent(MILLESTONE_REACHED);
            }
        }

    }

    public final void setMaterialOff(Material mat) {
        this.materialOff = mat;
    }

    public final void setMaterialOn(Material mat) {
        this.materialOn = mat;
    }
}
