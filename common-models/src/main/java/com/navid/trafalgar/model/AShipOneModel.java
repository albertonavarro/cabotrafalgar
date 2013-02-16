package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.statistics.FloatStatistic;
import com.navid.trafalgar.manager.statistics.Vector3fStatistic;
import com.navid.trafalgar.persistence.CandidateRecord;
import com.navid.trafalgar.persistence.StepRecord;

/**
 *
 * @author alberto
 */
public abstract class AShipOneModel extends AShipModel {

    public static class ShipCandidateRecord extends CandidateRecord<ShipSnapshot> {
    }

    @Override
    public final CandidateRecord getCandidateRecordInstance() {
        ShipCandidateRecord candidateRecord = new ShipCandidateRecord();
        candidateRecord.getHeader().setShipModel("ShipModelOneX");
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

    @Override
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

        private void resetRotation() {
            this.rotateY(-getRudderValue());
            value = 0;
        }
    }
    protected Spatial spatial;
    protected Sail sail;
    protected Rudder rudder;
    private Material matHull;
    private Material matSail;
    private float rudderRotation;
    private boolean previousTransparent = false;
    private FloatStatistic ropeLenght;
    private FloatStatistic speed;
    private FloatStatistic lastRudderRotation;
    private FloatStatistic lastSailRotation;
    private Vector3fStatistic shipDirection;
    public static String STATS_NAME = "shipOneStats";
    private float sailCorrection = 0.2f;
    private float sailRotateSpeed = 2f;
    private float shipInertia = 500;
    private float sailSurface = 100;
    private TimedIntertia  timedIntertia = new TimedIntertia();

    protected AShipOneModel(AssetManager assetManager, EventManager eventManager) {
        super(new Vector3f(1, 0, 0), assetManager, eventManager);

        initGeometry(assetManager, eventManager);

        this.attachChild(sail);
        this.attachChild(rudder);

        sail.setQueueBucket(Bucket.Transparent);
        spatial.setQueueBucket(Bucket.Transparent);
    }

    protected final void initStatisticsManager() {
        ropeLenght = statisticsManager.createStatistic(STATS_NAME, "Rope lenght", MINIMUM_ROPE);
        speed = statisticsManager.createStatistic(STATS_NAME, "Speed", 0f);
        lastRudderRotation = statisticsManager.createStatistic(STATS_NAME, "Rudder rotation", 0f);
        lastSailRotation = statisticsManager.createStatistic(STATS_NAME, "Sail rotation", 0f);
        shipDirection = statisticsManager.createStatistic(STATS_NAME, "Ship direction", this.getGlobalDirection());
    }

    protected abstract void initGeometry(AssetManager assetManager, EventManager eventManager);
    public static final float MINIMUM_ROPE = 1.5f;
    public static final float MAXIMUM_ROPE = 3;
    public static final float TRIMMING_SPEED = 1;

    private void updateSpeed(float tpf) {
        Vector3f windDirection = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        Vector3f shipOrientation3f = this.getGlobalDirection();

        double windOverVela = Math.cos(sail.getGlobalDirection().angleBetween(windDirection));

        float angleBetween = shipOrientation3f.angleBetween(sail.getGlobalDirection());
        float sailRegulation = (float) ((angleBetween < (Math.PI / 2)) ? -sailCorrection : sailCorrection);
        double velaOverShip = Math.cos(angleBetween + sailRegulation);

        float speedGain = (float) (windDirection.length() * windOverVela * velaOverShip * sailSurface);

        float speedLocal = timedIntertia.getAverage(tpf, speedGain);
        //float speedLocal2 = (speed.getValue() * shipInertia + speedGain) / (shipInertia + 1);

        speed.setValue(speedLocal);
    }

    private void updateRudder(float tpf) {
        if (rudderRotation == 0) {
            rudder.resetRotation();
        } else {
            rudder.rotateY(rudderRotation);
        }

        lastRudderRotation.setValue(rudderRotation);
        rudderRotation = 0f;
    }

    /**
     * Sail rotation.
     *
     * @param tpf
     */
    private void updateSailAutomaticRotation(float tpf) {
        Vector3f windDirection = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        Vector3f helperDirection = sail.getHelperDirection();
        Vector3f vectorshipDirection = this.getGlobalDirection();

        Vector3f resMultVectWindSail = helperDirection.cross(windDirection);
        Vector3f resMultVectSailShip = helperDirection.cross(vectorshipDirection);

        if (resMultVectWindSail.y * resMultVectSailShip.y > 0) {
            //Sail is moving towards the front
            if (helperDirection.angleBetween(vectorshipDirection) > ropeLenght.getValue()) {
                //Sail hasn't yet arrived to the limit
                sail.rotateY(resMultVectWindSail.y * tpf * sailRotateSpeed);
            } else {
                //Sail adjusts to the limit
                sail.rotateY(-Math.signum(resMultVectSailShip.y) * Math.abs(helperDirection.angleBetween(vectorshipDirection) - ropeLenght.getValue()) * tpf * sailRotateSpeed);
            }
        } else {
            //Sail is moving towards the back
            sail.rotateY(resMultVectWindSail.y * tpf * sailRotateSpeed);
        }
    }

    /**
     * Rotation on Z
     *
     * @param tpf
     */
    private void updateShipPitch(float tpf) {
    }

    /**
     * Rotation on Y
     *
     * @param tpf
     */
    private void updateShipYaw(float tpf) {
        this.rotate(0, rudder.getRudderValue() * speed.getValue() * tpf / 100, 0);
    }

    /**
     * Rotation on X
     *
     * @param tpf
     */
    private void updateShipRoll(float tpf) {
    }

    /**
     * Update position
     *
     * @param tpf
     */
    private void updatePosition(float tpf) {
        Vector3f shipOrientation3f = this.getGlobalDirection();
        this.move(shipOrientation3f.x * speed.getValue() * tpf, 0, shipOrientation3f.z * speed.getValue() * tpf);
        shipDirection.setValue(shipOrientation3f);
    }

    public final void update(float tpf) {
        updateSpeed(tpf);
        updateRudder(tpf);
        updateSailAutomaticRotation(tpf);
        updatePosition(tpf);
        updateShipPitch(tpf);
        updateShipYaw(tpf);
        updateShipRoll(tpf);
    }

    public void rudderRight(float tpf) {
        rudderRotation = -1 * tpf;
    }

    public void rudderLeft(float tpf) {
        rudderRotation = 1 * tpf;
    }

    public void sailTrim(float tpf) {
        if (ropeLenght.getValue() <= MINIMUM_ROPE + (1 * tpf)) {
            ropeLenght.setValue(MINIMUM_ROPE);
        } else {
            ropeLenght.setValue(ropeLenght.getValue() - (1 * tpf * TRIMMING_SPEED));
        }
    }

    public void sailLoose(float tpf) {
        if (ropeLenght.getValue() >= MAXIMUM_ROPE - (1 * tpf)) {
            ropeLenght.setValue(MAXIMUM_ROPE);
        } else {
            ropeLenght.setValue(ropeLenght.getValue() + (1 * tpf * TRIMMING_SPEED));
        }
    }
    private final AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("SHIP_RudderRight")) {
                rudderRight(tpf);
            }
            if (name.equals("SHIP_RudderLeft")) {
                rudderLeft(tpf);
            }
            if (name.equals("SHIP_SailTrim")) {
                sailTrim(tpf);
            }
            if (name.equals("SHIP_SailLoose")) {
                sailLoose(tpf);
            }
        }
    };

    @Override
    public void registerInput(InputManager inputManager) {
        inputManager.addListener(analogListener, new String[]{"SHIP_RudderRight", "SHIP_RudderLeft", "SHIP_SailTrim", "SHIP_SailLoose"});
    }

    @Override
    public final float getSpeed() {
        return speed.getValue();
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
    
}
