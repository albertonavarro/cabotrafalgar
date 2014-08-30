package com.navid.trafalgar.model;

import com.navid.trafalgar.input.Interactive;
import com.navid.trafalgar.input.Command;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.statistics.Auditable;
import com.navid.trafalgar.manager.statistics.Vector3fStatistic;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.StepRecord;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alberto
 */
public abstract class AShipModelTwo extends AShipModel implements Interactive {

    public static class ShipCandidateRecord extends CandidateRecord<ShipSnapshot> {
    }

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

    public final StepRecord getSnapshot() {
        ShipSnapshot snapshot = new ShipSnapshot();

        snapshot.setPosition(this.getLocalTranslation().clone());
        snapshot.setRotation(this.getLocalRotation().clone());

        return snapshot;
    }

    public final void updateFromRecord(StepRecord currentStepRecord) {
        ShipSnapshot snapshot = (ShipSnapshot) currentStepRecord;

        this.setLocalTranslation(snapshot.getPosition());
        this.setLocalRotation(snapshot.getRotation());
    }

    /**
     * Sail representation
     */
    protected abstract class Sail extends TrafalgarNode {

        private Node helperDirection;

        protected Sail(AssetManager assetManager, EventManager eventManager) {
            super(new Vector3f(0, 0, 1), assetManager, eventManager);
            helperDirection = new Node();
            helperDirection.rotateUpTo(new Vector3f(1, 0, 0));
            this.attachChild(helperDirection);
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

    protected abstract class Rudder extends TrafalgarNode {

        private float value = 0;

        protected Rudder(AssetManager assetManager, EventManager eventManager) {
            super(new Vector3f(1, 0, 0), assetManager, eventManager);
        }

        public final float getRudderValue() {
            return value;
        }

        public final void rotateY(float radians) {
            this.rotate(0, radians, 0);
            value += radians;
        }

        protected void resetRotation() {
            this.rotateY(-getRudderValue());
            value = 0;
        }
    }
    protected Spatial spatial;
    protected Sail sail;
    protected Rudder rudder;
    private Material matHull;
    private Material matSail;
    private boolean previousTransparent = false;
    
    
    
    
    
    protected float inclinacion = 0;

    
    public static String STATS_NAME = "shipOneStats";


    protected AShipModelTwo(String role, AssetManager assetManager, EventManager eventManager) {
        super(role, new Vector3f(1, 0, 0), assetManager, eventManager);

        initGeometry(assetManager, eventManager);

        this.attachChild(sail);
        this.attachChild(rudder);

        sail.setQueueBucket(Bucket.Transparent);
        spatial.setQueueBucket(Bucket.Transparent);
    }

    

    protected abstract void initGeometry(AssetManager assetManager, EventManager eventManager);
    
    public static final float MINIMUM_ROPE = 1.5f;
    public static final float MAXIMUM_ROPE = 3;
    public static final float TRIMMING_SPEED = 1;

    

    public final void setSailMaterial(Material mat) {
        matSail = mat;
        sail.setMaterial(mat);
    }

    public final void setHullMaterial(Material mat) {
        matHull = mat;
        spatial.setMaterial(mat);
    }

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

}
