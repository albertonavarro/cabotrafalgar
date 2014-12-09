package com.navid.trafalgar.shipmodely;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.model.TrafalgarNode;
import com.navid.trafalgar.model.IWind;
import com.navid.trafalgar.model.StepRecord;

/**
 *
 * @author alberto
 */
public abstract class AShipModelTwo extends AShipModel {

    public static class ShipCandidateRecord extends CandidateRecord<ShipSnapshot> {
    }

    @Override
    public final CandidateRecord getCandidateRecordInstance() {
        ShipCandidateRecord candidateRecord = new ShipCandidateRecord();
        candidateRecord.getHeader().setShipModel("ShipModelOneY");
        return candidateRecord;
    }

    /**
     * Internal representation for AShipOneModel
     */
    public static class ShipSnapshot extends StepRecord {

        private Vector3f position;
        private Quaternion rotation;

        private void setPosition(final Vector3f position) {
            this.position = position;
        }

        private void setRotation(final Quaternion rotation) {
            this.rotation = rotation;
        }

        public Vector3f getPosition() {
            return position;
        }

        public Quaternion getRotation() {
            return rotation;
        }
    }

    @Override
    public final StepRecord getSnapshot() {
        ShipSnapshot snapshot = new ShipSnapshot();

        snapshot.setPosition(this.getLocalTranslation().clone());
        snapshot.setRotation(this.getLocalRotation().clone());

        return snapshot;
    }

    protected Spatial spatial;
    protected Sail sail;
    protected Rudder rudder;
    private Material matHull;
    private Material matSail;
    private boolean previousTransparent = false;

    protected AShipModelTwo(String role, AssetManager assetManager, EventManager eventManager) {
        super(role, new Vector3f(1, 0, 0), assetManager, eventManager);

        spatial = assetManager.loadModel("Models/ship2g/ship2g.j3o");
        spatial.rotate(0f, (float) -Math.PI / 2, 0f);
        this.attachChild(spatial);

        sail = new Sail(assetManager, eventManager);
        rudder = new Rudder(assetManager, eventManager);

        this.attachChild(sail);
        this.attachChild(rudder);

        sail.setQueueBucket(Bucket.Transparent);
        spatial.setQueueBucket(Bucket.Transparent);
    }

    public final void setSailMaterial(Material mat) {
        matSail = mat;
        sail.setMaterial(mat);
    }

    public final void setHullMaterial(Material mat) {
        matHull = mat;
        spatial.setMaterial(mat);
    }

    @Override
    public final void setTransparent(boolean b) {
        if (!previousTransparent && b) {
            matHull.getAdditionalRenderState().setBlendMode(BlendMode.Color);
            matSail.getAdditionalRenderState().setBlendMode(BlendMode.Color);
            previousTransparent = true;

        } else if (!b && previousTransparent) {
            matHull.getAdditionalRenderState().setBlendMode(BlendMode.Off);
            matSail.getAdditionalRenderState().setBlendMode(BlendMode.Off);
            previousTransparent = false;
        }
    }

    @Override
    public void setWindNode(IWind.WindGeometry windGeometry) {
        this.attachChild(windGeometry);
        this.addControl(windGeometry);
        windGeometry.move(-10, 10, 0);
    }

    @Override
    public final void updateFromRecord(StepRecord currentStepRecord) {
        ShipSnapshot snapshot = (ShipSnapshot) currentStepRecord;

        this.setLocalTranslation(snapshot.getPosition());
        this.setLocalRotation(snapshot.getRotation());
    }

    /**
     * Sail representation
     */
    protected final class Sail extends TrafalgarNode {

        private final Node helperDirection;

        protected Sail(AssetManager assetManager, EventManager eventManager) {
            super(new Vector3f(0, 0, 1), assetManager, eventManager);
            helperDirection = new Node();
            helperDirection.rotateUpTo(new Vector3f(1, 0, 0));
            this.attachChild(helperDirection);
            Spatial s = ((Node) spatial).getChild("Cube.001");
            this.attachChild(s);
        }

        public Vector3f getHelperDirection() {
            return helperDirection.getWorldRotation().getRotationColumn(2);
        }

        public Node getHelperDirectionNode() {
            return helperDirection;
        }

        public final void rotateY(float radians) {
            this.rotate(0, radians, 0);
        }
    }

    protected final class Rudder extends TrafalgarNode {

        private final float MAXIMUM = 1;
        private float value = 0;

        protected Rudder(AssetManager assetManager, EventManager eventManager) {
            super(new Vector3f(1, 0, 0), assetManager, eventManager);
        }

        public final float getRudderValue() {
            return value;
        }

        public final void rotateY(float radians) {
            float increment;
            if (radians > 0) {
                if (value + radians > MAXIMUM) {
                    value = MAXIMUM;
                    increment = MAXIMUM - value;
                } else {
                    value += radians;
                    increment = radians;
                }
            } else {
                if (value + radians < -MAXIMUM) {
                    value = -MAXIMUM;
                    increment = -MAXIMUM + value;
                } else {
                    value += radians;
                    increment = radians;
                }
            }
            this.rotate(0, increment, 0);
        }

        protected void resetRotation() {
            this.rotateY(-getRudderValue());
            value = 0;
        }
    }

}
