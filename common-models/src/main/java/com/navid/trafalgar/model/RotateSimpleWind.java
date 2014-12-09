package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 *
 * @author alberto
 */
public final class RotateSimpleWind implements IWind {

    private ArrowModel arrowModel;
    private Vector2f vector = new Vector2f(1, 0);
    private AssetManager assetManager;

    public RotateSimpleWind(AssetManager assetManager) {
        this.assetManager = assetManager;
        arrowModel = new ArrowModel(assetManager, new Vector3f(1, 0, 0));
        setWind(new Vector2f(1, 0));
    }

    public Geometry getGeometry() {
        return arrowModel;
    }

    public void rotateWind(float angle) {
        vector.rotateAroundOrigin(angle, false);
        arrowModel.rotate(0, -angle, 0);
    }

    public Vector2f getWind() {
        return vector;
    }

    public void increase(float value) {
        vector.multLocal(value);
        arrowModel.scale(value);
    }

    public void setWind(Vector2f newVector) {
        Vector2f oldVector = this.vector;
        this.vector = newVector;

        arrowModel.rotate(0, newVector.angleBetween(oldVector), 0);
        arrowModel.scale(newVector.length() / oldVector.length());
    }

    public WindGeometry createWindGeometryNode() {
        return new WindGeometryImpl(assetManager);
    }

    public class WindGeometryImpl extends WindGeometry {

        ArrowModel arrowModel;

        public WindGeometryImpl(AssetManager assetManager) {
            Vector2f windVector = getWind();
            arrowModel = new ArrowModel(assetManager, new Vector3f(windVector.x, 0, windVector.y));
            arrowModel.scale(10);

            this.attachChild(arrowModel);
        }

        public void update(float f) {
            Vector3f currentWind = arrowModel.getWorldRotation().getRotationColumn(2);
            Vector2f currentWind2f = new Vector2f(currentWind.x, currentWind.z);
            float angle = currentWind2f.angleBetween(getWind());

            arrowModel.rotate(0, -angle, 0);
        }

        public void render(RenderManager rm, ViewPort vp) {
        }

        public Control cloneForSpatial(Spatial sptl) {
            return null;
        }

        public void setSpatial(Spatial sptl) {
        }

    }

}
