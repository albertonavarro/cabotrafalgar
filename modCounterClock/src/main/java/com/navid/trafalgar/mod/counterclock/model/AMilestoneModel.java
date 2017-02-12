package com.navid.trafalgar.mod.counterclock.model;

import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.AShipModelPlayer;
import com.navid.trafalgar.model.Dependent;
import com.navid.trafalgar.model.GameModel;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static com.navid.trafalgar.manager.EventManager.MILESTONE_REACHED;

public abstract class AMilestoneModel extends Node implements Control, Dependent {

    private EventManager eventManager;
    private boolean state = false;
    private Collection<? extends Node> collidables;
    private Material materialOn;
    private Material materialOff;

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
            this.collideWith(currentCollidable.getWorldBound(), results);

            if (!state && results.size() > 0) {
                state = true;
                eventManager.fireEvent(MILESTONE_REACHED);
            }
        }

    }

    public final void setMaterialOff(Material mat) {
        this.materialOff = mat;
    }

    public final void setMaterialOn(Material mat) {
        this.materialOn = mat;
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
