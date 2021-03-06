package com.navid.trafalgar.shipmodelz;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.statistics.Auditable;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.manager.statistics.Vector3fStatistic;
import com.navid.trafalgar.model.AShipModelPlayer;

public final class ShipModelZPlayer extends AShipModelZ implements AShipModelPlayer {

    public static final String STATS_NAME = "shipOneStats";

    public static final float MINIMUM_ROPE = 9;
    public static final float MAXIMUM_ROPE = 20;
    public static final float TRIMMING_SPEED = 5;
    public static final float MAX_HORIZONTAL_POS = 5;
    public static final float HORIZONTAL_WEIGHT_SPEED = 10;

    @Auditable
    private float windOverVela;
    @Auditable
    private float velaOverShip;
    @Auditable
    private float sailForcing;
    @Auditable
    private float acceleration;
    @Auditable
    private float friction;
    @Auditable
    private float rudderRotation;
    @Auditable
    private float ropeLenght = MAXIMUM_ROPE;
    @Auditable
    private float lastRudderRotation;
    @Auditable
    private float lastSailRotation;
    @Auditable
    private float mass = 1f;
    @Auditable
    private float localSpeed;
    @Auditable
    private float globalSpeed;
    @Auditable
    private float inclination = 0;
    @Auditable
    private float mainSheetDistance = 0;
    @Auditable
    private float horizontalPosition = 0;
    private float horizontalPositionIncrement = 0;

    private final float sailCorrection = 0.3f;
    private final float sailRotateSpeed = 2f;
    private final float sailSurface = 100;
    private float lastPitch = 0f;

    private Vector3fStatistic apparentWind;
    private Vector3fStatistic shipDirection;
    private Vector3fStatistic realWind;

    public ShipModelZPlayer(final AssetManager assetManager, EventManager eventManager) {
        super("Player", assetManager, eventManager);

        this.setHullMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md") {
            {
                setTexture("DiffuseMap", assetManager.loadTexture("Textures/wood.jpeg"));
            }
        });

        this.setSailMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md") {
            {
                setTexture("DiffuseMap", assetManager.loadTexture("Textures/sail.jpg"));
            }
        });
    }

    @Override
    public void setStatisticsManager(StatisticsManager statisticsManager) {
        shipDirection = statisticsManager.createStatistic(STATS_NAME, "Ship direction", this.getGlobalDirection());
        realWind = statisticsManager.createStatistic(STATS_NAME, "Real wind", Vector3f.UNIT_X);
        apparentWind = statisticsManager.createStatistic(STATS_NAME, "Apparent wind", Vector3f.UNIT_X);
    }

    private void updateSpeed(float tpf) {
        Vector3f realwindDirection3f = new Vector3f(getContext().getWind().getWind().x, 0, getContext().getWind().getWind().y);
        realwindDirection3f.multLocal(100);
        realWind.setValue(realwindDirection3f);

        Vector3f apparentWind3f = realwindDirection3f.subtract(this.getGlobalDirection().mult(localSpeed));
        apparentWind.setValue(apparentWind3f);

        Vector3f shipOrientation3f = this.getGlobalDirection();

        windOverVela = (float) Math.cos(getSail().getGlobalDirection().angleBetween(apparentWind3f.normalize()));

        float angleBetween = shipOrientation3f.angleBetween(getSail().getGlobalDirection());
        float sailRegulation = (float) ((angleBetween < (Math.PI / 2)) ? -sailCorrection : sailCorrection);
        velaOverShip = (float) Math.cos(angleBetween + sailRegulation);

        float force = (float) (apparentWind3f.length() * windOverVela * velaOverShip * sailForcing / ((1 + Math.abs(inclination)) / 2));

        acceleration = force / mass;
        friction = localSpeed * localSpeed / 80 * Math.signum(localSpeed);

        localSpeed += (acceleration - friction) * tpf;
    }

    private void updateRudder(float tpf) {
        if (rudderRotation == 0) {
            getRudder().resetRotation();
        } else {
            getRudder().rotateY(rudderRotation);
        }

        lastRudderRotation = rudderRotation;
        rudderRotation = 0f;
    }

    /**
     * Sail rotation.
     *
     * @param tpf
     */
    private void updateSailAutomaticRotation(float tpf) {
        Vector3f a = getMainsheetBoatHandler().getWorldTranslation();
        Vector3f b = getMainsheetSailHandler().getWorldTranslation();
        Vector3f difference = a.subtract(b);
        mainSheetDistance = difference.length();

        Vector3f windDirection = new Vector3f(getContext().getWind().getWind().x, 0, getContext().getWind().getWind().y);
        Vector3f helperDirection = getSail().getHelperDirection();
        Vector3f vectorshipDirection = this.getGlobalDirection();

        Vector3f resMultVectWindSail = helperDirection.cross(windDirection);
        Vector3f resMultVectSailShip = helperDirection.cross(vectorshipDirection);

        if (resMultVectWindSail.y * resMultVectSailShip.y > 0) {
            //Sail is moving towards the front
            if (mainSheetDistance < ropeLenght - 0.5) {
                //Sail hasn't yet arrived to the limit
                getSail().rotateY(resMultVectWindSail.y * tpf * sailRotateSpeed);
                sailForcing = 0f;
            } else if (mainSheetDistance < ropeLenght) {
                //Sail hasn't yet arrived to the limit
                sailForcing = 1f;
            } else {
                //Sail adjusts to the limit
                getSail().rotateY(
                        -Math.signum(resMultVectSailShip.y)
                        * Math.abs(helperDirection.angleBetween(vectorshipDirection) - ropeLenght)
                        * tpf
                        * sailRotateSpeed);
                sailForcing = 0f;
            }
        } else {
            if (mainSheetDistance > ropeLenght) {
                //Sail adjusts to the limit
                getSail().rotateY(
                        -Math.signum(resMultVectSailShip.y)
                        * Math.abs(helperDirection.angleBetween(vectorshipDirection) - ropeLenght)
                        * tpf
                        * sailRotateSpeed);
                sailForcing = 0f;
            } else {
                //Sail is moving backwards
                getSail().rotateY(resMultVectWindSail.y * tpf * sailRotateSpeed);
                sailForcing = 0f;
            }

        }
    }

    /**
     * Rotation on Z
     *
     * @param tpf
     */
    private void updateShipRoll(float tpf) {

        Vector3f windDirection = new Vector3f(getContext().getWind().getWind().x, 0, getContext().getWind().getWind().y);
        Vector3f shipOrientation3f = this.getGlobalDirection();

        double windOverVelaPitch = Math.cos(getSail().getGlobalDirection().angleBetween(windDirection));
        float angleBetween = shipOrientation3f.angleBetween(getSail().getGlobalDirection());

        double velaOverShipPitch = Math.sin(angleBetween);

        float windForce = (float) windOverVelaPitch * (float) velaOverShipPitch * sailForcing;
        float weightForce = horizontalPosition / 10;
        float keelForce = (float) Math.sin(inclination);
        float totalForce = (windForce + weightForce) - keelForce;

        inclination += totalForce * tpf;
        this.rotate(totalForce * tpf, 0, 0);

        lastPitch = totalForce;

    }

    /**
     * Rotation on Y
     *
     * @param tpf
     */
    private void updateShipYaw(float tpf) {
        this.setLocalRotation(
                new Quaternion().fromAngles(0, getRudder().getRudderValue() * localSpeed * tpf / 100, 0).mult(this.getLocalRotation()));
    }

    /**
     * Update local position
     *
     * @param tpf
     */
    private void updatePosition(float tpf) {
        Vector3f shipOrientation3f = this.getGlobalDirection();
        this.move(shipOrientation3f.x * localSpeed * tpf, 0, shipOrientation3f.z * localSpeed * tpf);
        this.move(getContext().getWater().getMovement(this.getGlobalDirection()).mult(-30 * tpf));
        shipDirection.setValue(shipOrientation3f);
    }

    private void updateWeightPosition(float tpf) {
        getWeight().move(0, 0, horizontalPositionIncrement);
        horizontalPositionIncrement = 0;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        updateWeightPosition(tpf);
        updateSpeed(tpf);
        updateRudder(tpf);
        updateSailAutomaticRotation(tpf);
        updatePosition(tpf);
        updateShipRoll(tpf);
        updateShipYaw(tpf);
    }

    public void rudderRight(float tpf) {
        rudderRotation = -1 * tpf;
    }

    public void rudderLeft(float tpf) {
        rudderRotation = 1 * tpf;
    }

    public void sailLoose(float tpf) {
        ropeLenght = getRangedValue(ropeLenght, tpf * TRIMMING_SPEED, MAXIMUM_ROPE, MINIMUM_ROPE);
    }

    public void sailTrim(float tpf) {
        ropeLenght = getRangedValue(ropeLenght, -tpf * TRIMMING_SPEED, MAXIMUM_ROPE, MINIMUM_ROPE);
    }

    public void weightPort(float tpf) {
        float previousPosition = horizontalPosition;
        horizontalPosition = getRangedValue(horizontalPosition, -tpf * HORIZONTAL_WEIGHT_SPEED, MAX_HORIZONTAL_POS, -MAX_HORIZONTAL_POS);
        horizontalPositionIncrement = horizontalPosition - previousPosition;
    }

    public void weightStarboard(float tpf) {
        float previousPosition = horizontalPosition;
        horizontalPosition = getRangedValue(horizontalPosition, tpf * HORIZONTAL_WEIGHT_SPEED, MAX_HORIZONTAL_POS, -MAX_HORIZONTAL_POS);
        horizontalPositionIncrement = horizontalPosition - previousPosition;
    }

    private float getRangedValue(float current, float increment, float max, float min) {
        current += increment;
        if (current > max) {
            return max;
        } else if (current < min) {
            return min;
        }

        return current;
    }

}
