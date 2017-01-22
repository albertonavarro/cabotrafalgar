package com.navid.trafalgar.shipmodely;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;
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
        candidateRecord.getHeader().setShipModel("ShipModelOneY");
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

        spatial = assetManager.loadModel("Models/ship2g/sail3.j3o");

        //spatial.scale(0.075f);
        this.attachChild(spatial);

        sail = new Sail(assetManager, eventManager);
        sail.move(18,0,0);
        spatial.rotate(0f, (float) -Math.PI / 2, 0f);
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
            Spatial boom = ((Node) spatial).getChild("boom");
            Spatial sail = ((Node) spatial).getChild("Plane");

            Material sailMat =  new Material(assetManager,"shaders/SailModelY.j3md");
            sailMat.getAdditionalRenderState().setWireframe(true);
            sailMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);

            Sphere sphereMesh = new Sphere(8, 8, 1f);
            Geometry mainsheetSailHandler1, mainsheetSailHandler2, mainsheetSailHandler3;
            mainsheetSailHandler1 = new Geometry("Mainsheet sail handler", sphereMesh);
            mainsheetSailHandler2 = new Geometry("Mainsheet sail handler", sphereMesh);
            mainsheetSailHandler3 = new Geometry("Mainsheet sail handler", sphereMesh);

            mainsheetSailHandler1.move(0, 9, 31);
            mainsheetSailHandler2.move(0, 57, 0);
            mainsheetSailHandler3.move(0, 9, 0);
            Material sphereMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mainsheetSailHandler1.setMaterial(sphereMat);
            mainsheetSailHandler2.setMaterial(sphereMat);
            mainsheetSailHandler3.setMaterial(sphereMat);
            this.attachChild(mainsheetSailHandler1);
            this.attachChild(mainsheetSailHandler2);
            this.attachChild(mainsheetSailHandler3);
            sailMat.setVector3("point1", mainsheetSailHandler1.getWorldTranslation());
            sailMat.setVector3("point2", mainsheetSailHandler2.getWorldTranslation());
            sailMat.setVector3("point3", mainsheetSailHandler3.getWorldTranslation());
            boom.setMaterial(sphereMat);
            boom.rotate((float) (Math.PI/2), 0, 0);
            boom.move(0, -1, 19);
            sail.setMaterial(sailMat);
            sail.rotate(0, 0,(float) (Math.PI/2));
            sail.move(-24, 0, 43);

            this.attachChild(sail);
            this.attachChild(boom);
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
