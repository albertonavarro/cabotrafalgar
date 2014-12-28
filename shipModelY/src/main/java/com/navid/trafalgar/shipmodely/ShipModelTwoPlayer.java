package com.navid.trafalgar.shipmodely;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.statistics.Auditable;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.manager.statistics.Vector3fStatistic;
import com.navid.trafalgar.model.AShipModelPlayer;

/**
 *
 * @author casa
 */
public final class ShipModelTwoPlayer extends AShipModelTwo implements AShipModelPlayer {

    public static final String STATS_NAME = "shipOneStats";

    public static final float MINIMUM_ROPE = 1.5f;
    public static final float MAXIMUM_ROPE = 3;
    public static final float TRIMMING_SPEED = 1;

    @Override
    public void setStatisticsManager(StatisticsManager statisticsManager) {
        shipDirection = statisticsManager.createStatistic(STATS_NAME, "Ship direction", this.getGlobalDirection());
        realWind = statisticsManager.createStatistic(STATS_NAME, "Real wind", Vector3f.UNIT_X);
        apparentWind = statisticsManager.createStatistic(STATS_NAME, "Apparent wind", Vector3f.UNIT_X);
    }

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
    private float ropeLenght = MINIMUM_ROPE;
    @Auditable
    private float lastRudderRotation;
    @Auditable
    private float lastSailRotation;
    @Auditable
    private float mass = 1f;
    @Auditable
    private float speed;
    @Auditable
    private float inclinacion = 0;

    private final float sailCorrection = 0.3f;
    private final float sailRotateSpeed = 2f;
    private final float sailSurface = 100;
    private float lastPitch = 0f;

    private Vector3fStatistic apparentWind;
    private Vector3fStatistic shipDirection;
    private Vector3fStatistic realWind;

    public ShipModelTwoPlayer(AssetManager assetManager, EventManager eventManager) {
        super("Player", assetManager, eventManager);
    }

    private void updateSpeed(float tpf) {
        Vector3f realwindDirection3f = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        realwindDirection3f.multLocal(100);
        realWind.setValue(realwindDirection3f);

        Vector3f apparentWind3f = realwindDirection3f.subtract(this.getGlobalDirection().mult(speed));
        apparentWind.setValue(apparentWind3f);

        Vector3f shipOrientation3f = this.getGlobalDirection();

        windOverVela = (float) Math.cos(getSail().getGlobalDirection().angleBetween(apparentWind3f.normalize()));

        float angleBetween = shipOrientation3f.angleBetween(getSail().getGlobalDirection());
        float sailRegulation = (float) ((angleBetween < (Math.PI / 2)) ? -sailCorrection : sailCorrection);
        velaOverShip = (float) Math.cos(angleBetween + sailRegulation);

        float force = (float) (apparentWind3f.length() * windOverVela * velaOverShip * sailForcing);

        acceleration = force / mass;
        friction = speed * speed / 80 * Math.signum(speed);

        float newspeed = speed + (acceleration - friction) * tpf;

        speed = newspeed;
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
        Vector3f windDirection = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        Vector3f helperDirection = getSail().getHelperDirection();
        Vector3f vectorshipDirection = this.getGlobalDirection();

        Vector3f resMultVectWindSail = helperDirection.cross(windDirection);
        Vector3f resMultVectSailShip = helperDirection.cross(vectorshipDirection);

        if (resMultVectWindSail.y * resMultVectSailShip.y > 0) {
            //Sail is moving towards the front
            if (helperDirection.angleBetween(vectorshipDirection) > ropeLenght) {
                //Sail hasn't yet arrived to the limit
                getSail().rotateY(resMultVectWindSail.y * tpf * sailRotateSpeed);
                sailForcing = 0f;
            } else {
                //Sail adjusts to the limit
                getSail().rotateY(-Math.signum(resMultVectSailShip.y)
                        * Math.abs(helperDirection.angleBetween(vectorshipDirection) - ropeLenght)
                        * tpf
                        * sailRotateSpeed);
                sailForcing = 1f;
            }
        } else {
            //Sail is moving towards the back
            getSail().rotateY(resMultVectWindSail.y * tpf * sailRotateSpeed);
            sailForcing = 0f;
        }
    }

    /**
     * Rotation on Z
     *
     * @param tpf
     */
    private void updateShipRoll(float tpf) {

        Vector3f windDirection = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        Vector3f shipOrientation3f = this.getGlobalDirection();

        double windOverVelaPitch = Math.cos(getSail().getGlobalDirection().angleBetween(windDirection));
        float angleBetween = shipOrientation3f.angleBetween(getSail().getGlobalDirection());

        double velaOverShipPitch = Math.sin(angleBetween);

        float targetPitch = (float) windOverVelaPitch * (float) velaOverShipPitch * sailForcing;
        float resistance = ((float) Math.sin(inclinacion));
        float totalForce = targetPitch - resistance;

        inclinacion += totalForce * tpf;
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
                new Quaternion().fromAngles(0, getRudder().getRudderValue()
                        * speed
                        * tpf / 100, 0)
                        .mult(this.getLocalRotation()));
    }

    /**
     * Update position
     *
     * @param tpf
     */
    private void updatePosition(float tpf) {
        Vector3f shipOrientation3f = this.getGlobalDirection();
        this.move(shipOrientation3f.x * speed * tpf, 0, shipOrientation3f.z * speed * tpf);
        shipDirection.setValue(shipOrientation3f);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
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
        if (ropeLenght <= MINIMUM_ROPE + (1 * tpf)) {
            ropeLenght = MINIMUM_ROPE;
        } else {
            ropeLenght = ropeLenght - (1 * tpf * TRIMMING_SPEED);
        }
    }

    public void sailTrim(float tpf) {
        if (ropeLenght >= MAXIMUM_ROPE - (1 * tpf)) {
            ropeLenght = MAXIMUM_ROPE;
        } else {
            ropeLenght = ropeLenght + (1 * tpf * TRIMMING_SPEED);
        }
    }

}
