package com.navid.trafalgar.model;

import com.google.common.collect.Sets;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.persistence.CandidateRecord;
import java.util.Set;

/**
 *
 * @author casa
 */
public class ShipModelTwoGhost extends AShipModelTwo {
    
    private final CandidateRecord<ShipSnapshot> candidateRecord;

    public ShipModelTwoGhost(   String role, 
                                AssetManager assetManager, 
                                EventManager eventManager, 
                                CandidateRecord<ShipSnapshot> candidateRecord) {
        super(role, assetManager, eventManager);
        this.candidateRecord = candidateRecord;
    }

    @Override
    public void registerInput(InputManager inputManager) {
    }

    @Override
    public Set<Command> getCommands() {
        return Sets.newHashSet();
    }

    @Override
    protected void initStatisticsManager() {
    }
    
    private final class Sail extends AShipModelTwo.Sail {

        public Sail(AssetManager assetManager, EventManager eventManager) {
            super(assetManager, eventManager);
            Spatial s = ((Node) spatial).getChild("Cube.001");
            this.attachChild(s);
        }
    }

    private final class Rudder extends AShipModelTwo.Rudder {

        public Rudder(AssetManager assetManager, EventManager eventManager) {
            super(assetManager, eventManager);
        }
    }
    
    @Override
    protected void initGeometry(AssetManager assetManager, EventManager eventManager) {
        spatial = assetManager.loadModel("Models/ship2g/ship2g.j3o");
        spatial.rotate(0f, (float) -Math.PI / 2, 0f);
        this.attachChild(spatial);

        sail = new ShipModelTwoGhost.Sail(assetManager, eventManager);
        rudder = new ShipModelTwoGhost.Rudder(assetManager, eventManager);
    }

    @Override
    public float getSpeed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void update(float tpf) {
       super.update(tpf);
       
       
    }
    
}