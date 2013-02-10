package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
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
public abstract class AShipZeroModel extends AShipModel {

    public static class ShipCandidateRecord extends CandidateRecord<ShipSnapshot> {
    }

    public static class ShipSnapshot extends StepRecord {

        private Vector3f position;
        private Quaternion rotation;

        private void setPosition(Vector3f position) {
            this.position = position;
        }

        private void setRotation(Quaternion rotation) {
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

    @Override
    public final CandidateRecord getCandidateRecordInstance() {
        ShipCandidateRecord candidateRecord = new ShipCandidateRecord();
        candidateRecord.getHeader().setShipModel("ModelZero");
        return candidateRecord;
    }

    protected abstract class Sail extends TrafalgarNode {

        protected Sail(AssetManager assetManager, EventManager eventManager) {
            super(new Vector3f(1, 0, 0), assetManager, eventManager);
        }

        public final void rotateY(float radians) {
            this.rotate(0, radians, 0);
        }
    }
    private static Vector3f reference = new Vector3f(1, 0, 0);

    protected abstract class Rudder extends TrafalgarNode {

        private float value = 0;

        protected Rudder(AssetManager assetManager, EventManager eventManager) {
            super(reference.clone(), assetManager, eventManager);
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
    private FloatStatistic speed;
    private FloatStatistic lastRudderRotation;
    private FloatStatistic lastSailRotation;
    private Vector3fStatistic shipDirection;
    public static String STATS_NAME = "shipZeroStats";
    private float rudderRotation;
    private float sailRotation;
    private boolean previousTransparent = false;
    private TimedIntertia  timedIntertia = new TimedIntertia();

    public AShipZeroModel(AssetManager assetManager, EventManager eventManager) {
        super(new Vector3f(1, 0, 0), assetManager, eventManager);

        initGeometry(assetManager);

        this.attachChild(sail);
        this.attachChild(rudder);

        spatial.setQueueBucket(Bucket.Transparent);
        sail.setQueueBucket(Bucket.Transparent);
    }

    protected final void initStatisticsManager() {
        speed = statisticsManager.createStatistic(STATS_NAME, "Speed", 0f);
        lastRudderRotation = statisticsManager.createStatistic(STATS_NAME, "Rudder rotation", 0f);
        lastSailRotation = statisticsManager.createStatistic(STATS_NAME, "Sail rotation", 0f);
        shipDirection = statisticsManager.createStatistic(STATS_NAME, "Ship direction", this.getGlobalDirection());
    }

    protected abstract void initGeometry(AssetManager assetManager);

    public final void update(float tpf) {

        if (rudderRotation == 0) {
            rudder.resetRotation();
        } else {
            rudder.rotateY(rudderRotation);
        }

        lastRudderRotation.setValue(rudderRotation);
        rudderRotation = 0f;

        sail.rotateY(sailRotation);
        lastSailRotation.setValue(sailRotation);
        sailRotation = 0;

        Vector3f windDirection = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        Vector3f shipOrientation3f = this.getGlobalDirection();

        double windOverVela = Math.cos(sail.getGlobalDirection().angleBetween(windDirection));
        double velaOverShip = Math.cos(shipOrientation3f.angleBetween(sail.getGlobalDirection()));

        float speedGain = (float) (windDirection.length() * windOverVela * velaOverShip * 100);

        float speedLocal = timedIntertia.getAverage(tpf, speedGain);
        //float speedLocal = (speed.getValue() * 500 + speed_gain) / 501;

        this.rotate(0, rudder.getRudderValue() * speedLocal * tpf / 100, 0);

        this.move(shipOrientation3f.x * speedLocal * tpf, 0, shipOrientation3f.z * speedLocal * tpf);

        speed.setValue(speedLocal);
        shipDirection.setValue(shipOrientation3f);


    }
    private final AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {

            if (name.equals("SHIP_RudderRight")) {
                rudderRotation = -tpf;
            }
            if (name.equals("SHIP_RudderLeft")) {
                rudderRotation = tpf;
            }
            if (name.equals("SHIP_SailRight")) {

                sailRotation = tpf;
            }
            if (name.equals("SHIP_SailLeft")) {
                sailRotation = -tpf;
            }

        }
    };

    @Override
    public final void registerInput(InputManager inputManager) {
        inputManager.addListener(analogListener, new String[]{"SHIP_RudderRight", "SHIP_RudderLeft", "SHIP_SailRight", "SHIP_SailLeft"});
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
            matHull.getAdditionalRenderState().setBlendMode(BlendMode.Color); // activate transparency
            matSail.getAdditionalRenderState().setBlendMode(BlendMode.Color); // activate transparency
            previousTransparent = true;

        } else if (!b && previousTransparent) {
            matHull.getAdditionalRenderState().setBlendMode(BlendMode.Off); // activate transparency
            matSail.getAdditionalRenderState().setBlendMode(BlendMode.Off); // activate transparency
            previousTransparent = false;
        }
    }

    @Override
    public void setWindNode(IWind.WindGeometry windGeometry) {
        this.attachChild(windGeometry);
        this.addControl(windGeometry);
        windGeometry.move(-10, 20, 0);
    }
}
