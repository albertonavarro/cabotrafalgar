package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.statistics.Auditable;
import com.navid.trafalgar.manager.statistics.Vector3fStatistic;
import static com.navid.trafalgar.model.AShipModelTwo.MAXIMUM_ROPE;
import static com.navid.trafalgar.model.AShipModelTwo.MINIMUM_ROPE;
import static com.navid.trafalgar.model.AShipModelTwo.TRIMMING_SPEED;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alberto
 */
public final class ShipModelTwo extends AShipModelTwo {

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
    private float sailCorrection = 0.3f;
    private float sailRotateSpeed = 2f;
    private float sailSurface = 100;
    private float lastPitch = 0f;
    private Vector3fStatistic apparentWind;
    private Vector3fStatistic shipDirection;
    private Vector3fStatistic realWind;
    @Auditable
    private float speed;

    @Override
    public void registerInput(InputManager inputManager) {
    }

    private final class Sail extends AShipModelTwo.Sail {

        public Sail(AssetManager assetManager, EventManager eventManager) {
            super(assetManager, eventManager);
            Spatial s = ((Node) spatial).getChild("Cube.001");
            this.attachChild(s);
        }
    }

    private final class Rudder extends AShipModelTwo.Rudder {

        public Rudder(AssetManager assetManager, EventManager eventManager) {
            super(assetManager, eventManager);
        }
    }

    public ShipModelTwo(String role, AssetManager assetManager, EventManager eventManager) {
        super(role, assetManager, eventManager);
    }

    @Override
    protected void initGeometry(AssetManager assetManager, EventManager eventManager) {
        spatial = assetManager.loadModel("Models/ship2g/ship2g.j3o");
        spatial.rotate(0f, (float) -Math.PI / 2, 0f);
        this.attachChild(spatial);

        sail = new Sail(assetManager, eventManager);
        rudder = new Rudder(assetManager, eventManager);
    }

    @Override
    public Set<Command> getCommands() {
        return new HashSet<Command>() {
            {
                add(new Command() {
                    @Override
                    public String toString() {
                        return "rudderLeft";
                    }

                    @Override
                    public void execute(float tpf) {
                        rudderRight(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "rudderRight";
                    }

                    @Override
                    public void execute(float tpf) {
                        rudderLeft(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "sailTrim";
                    }

                    @Override
                    public void execute(float tpf) {
                        sailTrim(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "sailLoose";
                    }

                    @Override
                    public void execute(float tpf) {
                        sailLoose(tpf);
                    }
                });
            }
        };
    }

    @Override
    protected final void initStatisticsManager() {
        shipDirection = statisticsManager.createStatistic(STATS_NAME, "Ship direction", this.getGlobalDirection());
        realWind = statisticsManager.createStatistic(STATS_NAME, "Real wind", Vector3f.UNIT_X);
        apparentWind = statisticsManager.createStatistic(STATS_NAME, "Apparent wind", Vector3f.UNIT_X);
    }

    @Override
    public final float getSpeed() {
        return speed;
    }
    
    private void updateSpeed(float tpf) {
        Vector3f realwindDirection3f = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        realwindDirection3f.multLocal(100);
        realWind.setValue(realwindDirection3f);

        Vector3f apparentWind3f = realwindDirection3f.subtract(this.getGlobalDirection().mult(speed));
        apparentWind.setValue(apparentWind3f);

        Vector3f shipOrientation3f = this.getGlobalDirection();

        windOverVela = (float) Math.cos(sail.getGlobalDirection().angleBetween(apparentWind3f.normalize()));

        float angleBetween = shipOrientation3f.angleBetween(sail.getGlobalDirection());
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
        Vector3f windDirection = new Vector3f(context.getWind().getWind().x, 0, context.getWind().getWind().y);
        Vector3f helperDirection = sail.getHelperDirection();
        Vector3f vectorshipDirection = this.getGlobalDirection();

        Vector3f resMultVectWindSail = helperDirection.cross(windDirection);
        Vector3f resMultVectSailShip = helperDirection.cross(vectorshipDirection);

        if (resMultVectWindSail.y * resMultVectSailShip.y > 0) {
            //Sail is moving towards the front
            if (helperDirection.angleBetween(vectorshipDirection) > ropeLenght) {
                //Sail hasn't yet arrived to the limit
                sail.rotateY(resMultVectWindSail.y * tpf * sailRotateSpeed);
                sailForcing = 0f;
            } else {
                //Sail adjusts to the limit
                sail.rotateY(-Math.signum(resMultVectSailShip.y) * Math.abs(helperDirection.angleBetween(vectorshipDirection) - ropeLenght) * tpf * sailRotateSpeed);
                sailForcing = 1f;
            }
        } else {
            //Sail is moving towards the back
            sail.rotateY(resMultVectWindSail.y * tpf * sailRotateSpeed);
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
        this.setLocalRotation(new Quaternion().fromAngles(0, rudder.getRudderValue() * speed * tpf / 100, 0).mult(this.getLocalRotation()));
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
    public final void update(float tpf) {
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
