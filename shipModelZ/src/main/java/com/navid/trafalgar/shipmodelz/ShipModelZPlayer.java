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

/**
 *
 * @author casa
 */
public class ShipModelZPlayer extends AShipModelZ implements AShipModelPlayer {

    public static String STATS_NAME = "shipOneStats";

    public static final float MINIMUM_ROPE = 9;
    public static final float MAXIMUM_ROPE = 20;
    public static final float TRIMMING_SPEED = 5; 
    public static final float MAXIMUM_RUDDER = 0.3f;

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
    private float localSpeed;
    @Auditable
    private float globalSpeed;
    @Auditable
    protected float inclinacion = 0;
    @Auditable
    protected float mainSheetDistance = 0;

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
        Vector3f realwindDirection3f = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        realwindDirection3f.multLocal(100);
        realWind.setValue(realwindDirection3f);

        Vector3f apparentWind3f = realwindDirection3f.subtract(this.getGlobalDirection().mult(localSpeed));
        apparentWind.setValue(apparentWind3f);

        Vector3f shipOrientation3f = this.getGlobalDirection();

        windOverVela = (float) Math.cos(sail.getGlobalDirection().angleBetween(apparentWind3f.normalize()));

        float angleBetween = shipOrientation3f.angleBetween(sail.getGlobalDirection());
        float sailRegulation = (float) ((angleBetween < (Math.PI / 2)) ? -sailCorrection : sailCorrection);
        velaOverShip = (float) Math.cos(angleBetween + sailRegulation);

        float force = (float) (apparentWind3f.length() * windOverVela * velaOverShip * sailForcing);

        acceleration = force / mass;
        friction = localSpeed * localSpeed / 80 * Math.signum(localSpeed);

        float newspeed = localSpeed + (acceleration - friction) * tpf;

        localSpeed = newspeed;
    }

    private void updateRudder(float tpf) {
        if (rudderRotation == 0) {
            rudder.resetRotation();
        } else {
            rudder.rotateY(rudderRotation);
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
        Vector3f a = mainsheetBoatHandler.getWorldTranslation();
        Vector3f b = mainsheetSailHandler.getWorldTranslation();
        Vector3f difference = a.subtract(b);
        mainSheetDistance = difference.length();
        
        Vector3f windDirection = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        Vector3f helperDirection = sail.getHelperDirection();
        Vector3f vectorshipDirection = this.getGlobalDirection();

        Vector3f resMultVectWindSail = helperDirection.cross(windDirection);
        Vector3f resMultVectSailShip = helperDirection.cross(vectorshipDirection);

        if (resMultVectWindSail.y * resMultVectSailShip.y > 0) {
            //Sail is moving towards the front
            if (mainSheetDistance < ropeLenght - 0.5) {
                //Sail hasn't yet arrived to the limit
                sail.rotateY(resMultVectWindSail.y * tpf * sailRotateSpeed);
                sailForcing = 0f;
            } else if (mainSheetDistance < ropeLenght) {
                //Sail hasn't yet arrived to the limit
                sailForcing = 1f;
            } else {
                //Sail adjusts to the limit
                sail.rotateY(-Math.signum(resMultVectSailShip.y) * Math.abs(helperDirection.angleBetween(vectorshipDirection) - ropeLenght) * tpf * sailRotateSpeed);
                sailForcing = 0f;
            }
        } else {
            if (mainSheetDistance > ropeLenght) {
                //Sail adjusts to the limit
                sail.rotateY(-Math.signum(resMultVectSailShip.y) * Math.abs(helperDirection.angleBetween(vectorshipDirection) - ropeLenght) * tpf * sailRotateSpeed);
                sailForcing = 0f;
            } else {
                //Sail is moving backwards
                sail.rotateY(resMultVectWindSail.y * tpf * sailRotateSpeed);
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

        Vector3f windDirection = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        Vector3f shipOrientation3f = this.getGlobalDirection();

        double windOverVelaPitch = Math.cos(sail.getGlobalDirection().angleBetween(windDirection));
        float angleBetween = shipOrientation3f.angleBetween(sail.getGlobalDirection());

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
        this.setLocalRotation(new Quaternion().fromAngles(0, rudder.getRudderValue() * localSpeed * tpf / 100, 0).mult(this.getLocalRotation()));
    }

    /**
     * Update local position
     *
     * @param tpf
     */
    private void updateLocalPosition(float tpf) {
        Vector3f shipOrientation3f = this.getGlobalDirection();
        this.move(shipOrientation3f.x * localSpeed * tpf, 0, shipOrientation3f.z * localSpeed * tpf);
        this.move(context.getWater().getMovement(this.getGlobalDirection()).mult(-30*tpf));
        shipDirection.setValue(shipOrientation3f);
    }

    @Override
    public final void update(float tpf) {
        super.update(tpf);
        updateSpeed(tpf);
        updateRudder(tpf);
        updateSailAutomaticRotation(tpf);
        updateLocalPosition(tpf);
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
        if (ropeLenght >= MAXIMUM_ROPE - (1 * tpf)) {
            ropeLenght = MAXIMUM_ROPE;
        } else {
            ropeLenght = ropeLenght + (1 * tpf * TRIMMING_SPEED);
        }
    }

    public void sailTrim(float tpf) {
        if (ropeLenght <= MINIMUM_ROPE + (1 * tpf)) {
            ropeLenght = MINIMUM_ROPE;
        } else {
            ropeLenght = ropeLenght - (1 * tpf * TRIMMING_SPEED);
        }
    }

    void weightPort(float tpf) {

    }

    void weightStarboard(float tpf) {

    }

}
