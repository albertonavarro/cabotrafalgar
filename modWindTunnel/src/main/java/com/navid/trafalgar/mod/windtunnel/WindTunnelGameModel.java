package com.navid.trafalgar.mod.windtunnel;

import com.jme3.light.AmbientLight;
import com.jme3.post.Filter;
import com.jme3.scene.Node;
import com.navid.trafalgar.model.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author alberto
 */
public class WindTunnelGameModel {

    private AShipModel ship;
    private IContext context;
    
    private Node gameNode = new Node("model");
    private List<Filter> fpp = new ArrayList<Filter>();
    
    private boolean inited = false;
    
    public boolean isInited(){
        return inited;
    }
    
    public void init(GameModel gameModel) {
        
        if(inited){
            throw new IllegalStateException("Instance CounterClockGameModel already inited");
        }
        
        inited = true;
        
        ship = (AShipModel) gameModel.getByType(AShipModel.class).iterator().next();
        context = (IContext) gameModel.getSingleByType(IContext.class);
        fpp = gameModel.getByType(Filter.class);
        
        gameNode.addLight( (SunModel) gameModel.getSingleByType(SunModel.class));
        
        gameNode.attachChild(ship);
        gameNode.addLight(new AmbientLight());
        gameNode.attachChild((SkyModel) gameModel.getSingleByType(SkyModel.class));
    }
    
    /**
     * @return the gameNode
     */
    public Node getGameNode() {
        if(!inited){
            throw new IllegalStateException("Instance WindTunnelGameModel not yet inited");
        }
        
        return gameNode;
    }

    /**
     * @return the fpp
     */
    public Collection<Filter> getFpp() {
        if(!inited){
            throw new IllegalStateException("Instance WindTunnelGameModel not yet inited");
        }
        
        return fpp;
    }

    /**
     * @return the ship
     */
    public AShipModel getShip() {
        return ship;
    }

    /**
     * 
     * @return 
     */
    public IContext getIContext() {
        return context;
    }

}
