package com.navid.trafalgar.mod.counterclock;

import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.post.Filter;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.input.SystemInteractions;
import com.navid.trafalgar.mod.counterclock.model.AMilestoneModel;
import com.navid.trafalgar.model.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CounterClockGameModel {

    private final Node gameNode = new Node("reflexion");

    private List<AMilestoneModel> milestones;
    private IContext context;
    private List<Filter> fpp = new ArrayList<Filter>();
    private boolean inited = false;
    private AShipModelPlayer ship;
    private AShipModelGhost ghost;

    public boolean isInited() {
        return inited;
    }

    public void init(GameModel gameModel, GameModel preGameModel) {

        if (inited) {
            throw new IllegalStateException("Instance CounterClockGameModel already inited");
        }

        inited = true;

        ship = gameModel.getSingleByTypeAndName(AShipModelPlayer.class, "player1");

        if (gameModel.contains(AShipModelGhost.class)) {
            ghost = gameModel.getSingleByType(AShipModelGhost.class);
        }

        milestones = gameModel.getByType(AMilestoneModel.class);

        context = gameModel.getSingleByType(IContext.class);

        fpp = gameModel.getByType(Filter.class);

        for (Spatial currentNode : gameModel.getByType(Spatial.class)) {
            gameNode.attachChild(currentNode);
        }

        for (Control control : gameModel.getByType(Control.class)) {
            gameNode.addControl(control);
        }

        for (Light light : gameModel.getByType(Light.class)) {
            gameNode.addLight(light);
        }

        gameNode.addLight(new AmbientLight());
        fpp.add(new TranslucentBucketFilter());
    }

    /**
     * @return the gameNode
     */
    public Node getGameNode() {
        if (!inited) {
            throw new IllegalStateException("Instance CounterClockGameModel not yet inited");
        }

        return gameNode;
    }

    /**
     * @return the fpp
     */
    public Collection<Filter> getFpp() {
        if (!inited) {
            throw new IllegalStateException("Instance CounterClockGameModel not yet inited");
        }

        return fpp;
    }

    /**
     * @return the ship
     */
    public AShipModelPlayer getShip() {
        return ship;
    }

    /**
     *
     * @return
     */
    public IContext getIContext() {
        return context;
    }

    /**
     * @return the milestones
     */
    public List<AMilestoneModel> getMilestones() {
        return milestones;
    }

    /**
     * @return the ghost
     */
    public AShipModelGhost getGhost() {
        return ghost;
    }
}
