package com.navid.trafalgar.model;

import com.jme3.math.Vector3f;
import com.jme3.water.WaterFilter;

public final class SimpleWater extends WaterFilter implements IWater {

    private final Vector3f movement = new Vector3f();

    public SimpleWater() {
        setWaveScale(0.003f);
        setMaxAmplitude(2f);
        setFoamExistence(new Vector3f(1f, 4, 0.5f));
        setNormalScale(0.5f);
        setRefractionConstant(0.25f);
        setRefractionStrength(0.2f);
        setFoamHardness(0.6f);
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public Vector3f getMovement(Vector3f position) {
        return movement;
    }

}
