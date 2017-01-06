package com.navid.trafalgar.mod.windtunnel;

import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.post.Filter;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.input.SystemInteractions;
import com.navid.trafalgar.mod.windtunnel.model.AHarnessModel;
import com.navid.trafalgar.model.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class WindTunnelGameModel {

    private AShipModelPlayer ship;
    private IContext context;
    private AHarnessModel harness;

    private final Node gameNode = new Node("model");
    private List<Filter> fpp = new ArrayList<Filter>();

    private boolean inited = false;

    public boolean isInited() {
        return inited;
    }

    public void init(GameModel gameModel, GameModel preGameModel) {

        if (inited) {
            throw new IllegalStateException("Instance CounterClockGameModel already inited");
        }

        inited = true;

        ship = gameModel.getSingleByType(AShipModelPlayer.class);
        context = gameModel.getSingleByType(IContext.class);
        harness = gameModel.getSingleByType(AHarnessModel.class);

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
            throw new IllegalStateException("Instance WindTunnelGameModel not yet inited");
        }

        return gameNode;
    }

    /**
     * @return the fpp
     */
    public Collection<Filter> getFpp() {
        if (!inited) {
            throw new IllegalStateException("Instance WindTunnelGameModel not yet inited");
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
     * @return the harness
     */
    public AHarnessModel getHarness() {
        return harness;
    }

    /**
     * @param harness the harness to set
     */
    public void setHarness(AHarnessModel harness) {
        this.harness = harness;
    }

}
