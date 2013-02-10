package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.AShipZeroModel;

/**
 *
 * @author alberto
 */
public final class ShipZeroModel extends AShipZeroModel {

    

    private final class Sail extends AShipZeroModel.Sail {

        public Sail(AssetManager assetManager, EventManager eventManager) {
            super(assetManager, eventManager);
            
            Geometry geoSail = new Geometry("vela", new Box(0.1f, 13f, 13f));
            this.attachChild(geoSail);
            this.move(0, 25, 0);
        }
    }

    private final class Rudder extends AShipZeroModel.Rudder {
        public Rudder(AssetManager assetManager, EventManager eventManager) {
            super(assetManager, eventManager);
        }
    }

    public ShipZeroModel(AssetManager assetManager, EventManager eventManager) {
        super(assetManager, eventManager);
    }

    @Override
    protected void initGeometry(AssetManager assetManager) {
        spatial = assetManager.loadModel("Models/s4/s4.j3o");
        spatial.rotate(0f, (float) Math.PI / 2, 0f);
        spatial.move(0, 1, 0);
        this.attachChild(spatial);
        
        sail = new Sail(assetManager, eventManager);
        rudder = new Rudder(assetManager, eventManager);

    }
}
