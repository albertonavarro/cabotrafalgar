package com.navid.trafalgar.mod.counterclock;

import com.jme3.light.AmbientLight;
import com.jme3.post.Filter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.navid.trafalgar.mod.counterclock.model.AMillestoneModel;
import com.navid.trafalgar.model.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 *
 * @author alberto
 */
public class CounterClockGameModel {

    private List<AMillestoneModel> millestones;
    private AShipModel ship;
    private IContext context;
    
    private Node gameNode = new Node("reflexion");
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
        millestones = gameModel.getByType(AMillestoneModel.class);
        context = (IContext) gameModel.getSingleByType(IContext.class);
        fpp = gameModel.getByType(Filter.class);
        
        gameNode.addLight( (SunModel) gameModel.getSingleByType(SunModel.class));
        
        gameNode.attachChild(ship);
        
        
        for(Spatial currentNode : gameModel.getByType(Spatial.class)){
            gameNode.attachChild(currentNode);
        }
        
        gameNode.addLight(new AmbientLight());
        
       // gameNode.attachChild((SkyModel) gameModel.getSingleByType(SkyModel.class));
    }
    
    /**
     * @return the gameNode
     */
    public Node getGameNode() {
        if(!inited){
            throw new IllegalStateException("Instance CounterClockGameModel not yet inited");
        }
        
        return gameNode;
    }

    /**
     * @return the fpp
     */
    public Collection<Filter> getFpp() {
        if(!inited){
            throw new IllegalStateException("Instance CounterClockGameModel not yet inited");
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

    /**
     * @return the millestones
     */
    public List<AMillestoneModel> getMillestones() {
        return millestones;
    }
}
