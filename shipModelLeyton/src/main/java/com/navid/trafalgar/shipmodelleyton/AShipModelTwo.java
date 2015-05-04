package com.navid.trafalgar.shipmodelleyton;

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

public abstract class AShipModelTwo extends AShipModel {

    public static class ShipCandidateRecord extends CandidateRecord<ShipSnapshot> {
    }

    @Override
    public final CandidateRecord getCandidateRecordInstance() {
        ShipCandidateRecord candidateRecord = new ShipCandidateRecord();
        candidateRecord.getHeader().setShipModel("ShipModelLeyton");
        return candidateRecord;
    }

    /**
     * Internal representation for AShipOneModel
     */
    public static final class ShipSnapshot extends StepRecord {

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

    private final Spatial spatial;
    private final Sail sail;
    private final Rudder rudder;
    private Material matHull;
    private Material matSail;
    private boolean previousTransparent = false;

    protected AShipModelTwo(String role, AssetManager assetManager, EventManager eventManager) {
        super(role, new Vector3f(1, 0, 0), assetManager, eventManager);

        spatial = assetManager.loadModel("com/navid/trafalgar/shipmodelleyton/y/V70.obj");
        spatial.rotate(0f, (float) Math.PI / 2, 0f);
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
            //matHull.getAdditionalRenderState().setBlendMode(BlendMode.Color);
            //matSail.getAdditionalRenderState().setBlendMode(BlendMode.Color);
            previousTransparent = true;

        } else if (!b && previousTransparent) {
            //matHull.getAdditionalRenderState().setBlendMode(BlendMode.Off);
            //matSail.getAdditionalRenderState().setBlendMode(BlendMode.Off);
            previousTransparent = false;
        }
    }

    @Override
    public final void setWindNode(IWind.WindGeometry windGeometry) {
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
            super.setDebug(true);
            helperDirection = new Node();
            helperDirection.rotateUpTo(new Vector3f(1, 0, 0));
            this.attachChild(helperDirection);
            Spatial mast = ((Node) spatial).getChild("V70-geom-4");
            Spatial spatialSail = assetManager.loadModel("com/navid/trafalgar/shipmodelleyton/y/Vela.obj");
            
            this.attachChild(spatialSail);
            mast.setLocalTranslation(new Vector3f(1.3f, 10f, 34.5f));
            mast.rotate(0f, (float) Math.PI, 0f);
            spatialSail.setLocalTranslation(new Vector3f(1.3f, 10f, 34.5f));
            spatialSail.rotate(0f, (float) Math.PI, 0f);
            this.attachChild(mast);


        }

        public Vector3f getHelperDirection() {
            return helperDirection.getWorldRotation().getRotationColumn(2);
        }

        public Node getHelperDirectionNode() {
            return helperDirection;
        }

        public void rotateY(float radians) {
            this.rotate(0, radians, 0);
        }
    }

    protected final class Rudder extends TrafalgarNode {

        private static final float MAXIMUM = 1;
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

}