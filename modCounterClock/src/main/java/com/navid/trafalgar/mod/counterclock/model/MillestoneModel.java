package com.navid.trafalgar.mod.counterclock.model;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class MillestoneModel extends AMillestoneModel {

    private RigidBodyControl millestone;
    private Spatial spatial;

    public MillestoneModel(AssetManager assetManager) {
        super();
        spatial = assetManager.loadModel("Models/m1/m1.j3o");
        spatial.scale(3);
        this.attachChild(spatial);

        CollisionShape sceneShape
                = CollisionShapeFactory.createMeshShape(this);
        millestone = new RigidBodyControl(sceneShape, 0);
        this.addControl(millestone);
        millestone.setPhysicsLocation(new Vector3f(-200, 0, 100));

    }

    @Override
    public void setLocalTranslation(Vector3f pos) {
        super.setLocalTranslation(pos);
        millestone.setPhysicsLocation(pos);
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSpatial(Spatial spatial) {
    }

    public void render(RenderManager rm, ViewPort vp) {
    }

    public boolean isEnabled() {
        return true;
    }

    public void setEnabled(boolean value) {
        //todo
    }
}
