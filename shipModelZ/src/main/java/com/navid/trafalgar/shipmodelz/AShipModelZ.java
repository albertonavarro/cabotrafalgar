package com.navid.trafalgar.shipmodelz;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.model.TrafalgarNode;
import com.navid.trafalgar.model.IWind;
import com.navid.trafalgar.model.StepRecord;

public abstract class AShipModelZ extends AShipModel {

    private final Spatial spatial;
    private final Sail sail;
    private final Rudder rudder;
    private Material matHull;
    private Material matSail;
    private boolean previousTransparent = false;
    private final Geometry mainsheetBoatHandler;
    private Geometry mainsheetSailHandler;
    private final Geometry weight;

    protected AShipModelZ(String role, AssetManager assetManager, EventManager eventManager) {
        super(role, new Vector3f(1, 0, 0), assetManager, eventManager);

        spatial = assetManager.loadModel("Models/ship2z/ship2g.j3o");
        spatial.rotate(0f, (float) -Math.PI / 2, 0f);
        this.attachChild(spatial);

        Sphere sphereMesh = new Sphere(8, 8, 1f);
        mainsheetBoatHandler = new Geometry("Mainsheet boat handler", sphereMesh);
        mainsheetBoatHandler.move(-20, 2, 0);
        Material sphereMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mainsheetBoatHandler.setMaterial(sphereMat);
        this.attachChild(mainsheetBoatHandler);

        Sphere sphereWeight = new Sphere(8, 8, 5f);
        weight = new Geometry("Mainsheet boat handler", sphereWeight);
        weight.move(-20, 3, 0);
        Material sphereWeightMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        weight.setMaterial(sphereWeightMat);
        this.attachChild(weight);

        sail = new Sail(assetManager, eventManager);
        rudder = new Rudder(assetManager, eventManager);

        this.attachChild(sail);
        this.attachChild(rudder);

        sail.setQueueBucket(Bucket.Transparent);
        spatial.setQueueBucket(Bucket.Transparent);
    }

    @Override
    public final CandidateRecord getCandidateRecordInstance() {
        ShipCandidateRecord candidateRecord = new ShipCandidateRecord();
        candidateRecord.getHeader().setShipModel("ShipModelOneZ");
        return candidateRecord;
    }

    @Override
    public final StepRecord getSnapshot() {
        ShipSnapshot snapshot = new ShipSnapshot();

        snapshot.setPosition(this.getLocalTranslation().clone());
        snapshot.setRotation(this.getLocalRotation().clone());
        snapshot.setLastRotation(getSail().getLastRotation());

        return snapshot;
    }

    public final void setSailMaterial(Material mat) {
        matSail = mat;
        getSail().setMaterial(mat);
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
    public final void setWindNode(IWind windGeometry) {
        this.attachChild(windGeometry);
        this.addControl(windGeometry);
        windGeometry.move(-10, 10, 0);
    }

    @Override
    public final void updateFromRecord(StepRecord currentStepRecord) {
        ShipSnapshot snapshot = (ShipSnapshot) currentStepRecord;

        this.setLocalTranslation(snapshot.getPosition());
        this.setLocalRotation(snapshot.getRotation());
        getSail().rotateY(snapshot.getLastRotation());
    }

    /**
     * @return the sail
     */
    public final Sail getSail() {
        return sail;
    }

    /**
     * @return the rudder
     */
    public final Rudder getRudder() {
        return rudder;
    }

    /**
     * @return the weight
     */
    public final Geometry getWeight() {
        return weight;
    }

    /**
     * @return the mainsheetBoatHandler
     */
    public final Geometry getMainsheetBoatHandler() {
        return mainsheetBoatHandler;
    }

    /**
     * @return the mainsheetSailHandler
     */
    public final Geometry getMainsheetSailHandler() {
        return mainsheetSailHandler;
    }

    /**
     * Sail representation
     */
    protected final class Sail extends TrafalgarNode {

        private final Node helperDirection;

        private float lastRotation = 0;

        protected Sail(AssetManager assetManager, EventManager eventManager) {
            super(new Vector3f(0, 0, 1), assetManager, eventManager);
            helperDirection = new Node();
            helperDirection.rotateUpTo(new Vector3f(1, 0, 0));
            this.attachChild(helperDirection);
            Spatial s = ((Node) spatial).getChild("Cube.001");
            this.attachChild(s);

            Sphere sphereMesh = new Sphere(8, 8, 1f);
            mainsheetSailHandler = new Geometry("Mainsheet sail handler", sphereMesh);
            getMainsheetSailHandler().move(0, 9, 15);
            Material sphereMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            getMainsheetSailHandler().setMaterial(sphereMat);
            this.attachChild(getMainsheetSailHandler());
            this.rotateY(-0.5f);
        }

        public Vector3f getHelperDirection() {
            return helperDirection.getWorldRotation().getRotationColumn(2);
        }

        public Node getHelperDirectionNode() {
            return helperDirection;
        }

        public void rotateY(float radians) {
            lastRotation += radians;
            this.rotate(0, radians, 0);
        }

        public float getLastRotation() {
            float t = lastRotation;
            lastRotation = 0;
            return t;
        }
    }

    protected final class Rudder extends TrafalgarNode {

        private static final float MAXIMUM_RUDDER = 2;
        private float value = 0;

        protected Rudder(AssetManager assetManager, EventManager eventManager) {
            super(new Vector3f(1, 0, 0), assetManager, eventManager);
        }

        public float getRudderValue() {
            return value;
        }

        public void rotateY(float radians) {
            float increment;
            if (radians > 0) {
                if (value + radians > MAXIMUM_RUDDER) {
                    value = MAXIMUM_RUDDER;
                    increment = MAXIMUM_RUDDER - value;
                } else {
                    value += radians;
                    increment = radians;
                }
            } else {
                if (value + radians < -MAXIMUM_RUDDER) {
                    value = -MAXIMUM_RUDDER;
                    increment = -MAXIMUM_RUDDER + value;
                } else {
                    value += radians;
                    increment = radians;
                }
            }
            this.rotate(0, increment, 0);
        }

        protected void resetRotation() {
            value = 0;
            this.rotateY(-value);
        }
    }

    public static class ShipCandidateRecord extends CandidateRecord<ShipSnapshot> {
    }

    /**
     * Internal representation for AShipOneModel
     */
    public static final class ShipSnapshot extends StepRecord {

        private Vector3f position;
        private Quaternion rotation;
        private float lastRotation;

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

        public float getLastRotation() {
            return lastRotation;
        }

        public void setLastRotation(float lastRotation) {
            this.lastRotation = lastRotation;
        }
    }

}
