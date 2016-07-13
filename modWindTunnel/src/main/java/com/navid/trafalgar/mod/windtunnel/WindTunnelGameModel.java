package com.navid.trafalgar.mod.windtunnel;

import com.jme3.light.AmbientLight;
import com.jme3.post.Filter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
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

        ship = (AShipModelPlayer) gameModel.getSingleByType(AShipModelPlayer.class);
        preGameModel.getSingleByTypeAndName(AShipModelInteractive.class, "player1").setTarget(ship);

        SystemInteractions systemInteractions = preGameModel.getSingleByTypeAndName(SystemInteractions.class, "system");
        systemInteractions.setTarget(preGameModel.getSingleByTypeAndName(AShipModelPlayer.class, "system"));

        context = (IContext) gameModel.getSingleByType(IContext.class);
        harness = (AHarnessModel) gameModel.getSingleByType(AHarnessModel.class);

        gameNode.addLight((SunModel) gameModel.getSingleByType(SunModel.class));

        fpp = gameModel.getByType(Filter.class);

        gameNode.attachChild((Spatial) ship);
        gameNode.addLight(new AmbientLight());
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
