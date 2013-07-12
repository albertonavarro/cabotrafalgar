package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.navid.trafalgar.manager.EventManager;

/**
 *
 * @author alberto
 */
public final class ShipModelTwo extends AShipModelTwo {

    @Override
    public void registerInput(InputManager inputManager) {
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

    public ShipModelTwo(AssetManager assetManager, EventManager eventManager) {
        super(assetManager, eventManager);
    }

    @Override
    protected void initGeometry(AssetManager assetManager, EventManager eventManager) {
        spatial = assetManager.loadModel("Models/ship2g/ship2g.j3o");
        spatial.rotate(0f, (float) -Math.PI / 2, 0f);
        this.attachChild(spatial);

        sail = new Sail(assetManager, eventManager);
        rudder = new Rudder(assetManager, eventManager);
    }
}
