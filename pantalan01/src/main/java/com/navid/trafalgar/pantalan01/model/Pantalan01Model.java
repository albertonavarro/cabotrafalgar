package com.navid.trafalgar.pantalan01.model;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.model.GameModel;

public final class Pantalan01Model extends APantalan01Model {

    private final RigidBodyControl milestone;
    private final Spatial spatial;

    public Pantalan01Model(AssetManager assetManager) {
        super();
        spatial = assetManager.loadModel("Models/pantalan01/Pantalan_completo2.obj");
        spatial.scale(5);
        this.attachChild(spatial);

        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(this);
        milestone = new RigidBodyControl(sceneShape, 0);
        this.addControl(milestone);
    }

    @Override
    public void setLocalTranslation(Vector3f pos) {
        super.setLocalTranslation(pos);
        milestone.setPhysicsLocation(pos);
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSpatial(Spatial spatial) {
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
    }

    public boolean isEnabled() {
        return true;
    }

    public void setEnabled(boolean value) {
        //todo
    }
}
