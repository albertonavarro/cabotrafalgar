package com.navid.trafalgar.mod.tutorial;

import com.jme3.light.AmbientLight;
import com.jme3.post.Filter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.navid.trafalgar.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class TutorialGameModel {

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
            throw new IllegalStateException("Instance TutorialGameModel` already inited");
        }

        inited = true;

        ship = gameModel.getSingleByType(AShipModelPlayer.class);
        gameNode.attachChild((Spatial) ship);
        preGameModel.getSingleByType(AShipModelInteractive.class).setTarget(ship);

        if (gameModel.contains(AShipModelGhost.class)) {
            ghost = gameModel.getSingleByType(AShipModelGhost.class);
            if (ghost != null) {
                gameNode.attachChild((Spatial) ghost);
            }
        }

        milestones = gameModel.getByType(AMilestoneModel.class);
        context = (IContext) gameModel.getSingleByType(IContext.class);
        fpp = gameModel.getByType(Filter.class);

        gameNode.addLight((SunModel) gameModel.getSingleByType(SunModel.class));

        for (Spatial currentNode : gameModel.getByType(Spatial.class)) {
            gameNode.attachChild(currentNode);
        }

        gameNode.addLight(new AmbientLight());
    }

    /**
     * @return the gameNode
     */
    public Node getGameNode() {
        if (!inited) {
            throw new IllegalStateException("Instance TutorialGameModel not yet inited");
        }

        return gameNode;
    }

    /**
     * @return the fpp
     */
    public Collection<Filter> getFpp() {
        if (!inited) {
            throw new IllegalStateException("Instance TutorialGameModel not yet inited");
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