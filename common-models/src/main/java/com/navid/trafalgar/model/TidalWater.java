package com.navid.trafalgar.model;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.water.WaterFilter;

public final class TidalWater extends WaterFilter implements IWater {
    
    private Vector3f speed;

    public TidalWater(Vector2f speed) {
        this.speed = new Vector3f(speed.x, 0, speed.y);
        setWaveScale(0.003f);
        setMaxAmplitude(2f);
        setFoamExistence(new Vector3f(1f, 4, 0.5f));
        setNormalScale(0.5f);
        setRefractionConstant(0.25f);
        setRefractionStrength(0.2f);
        setFoamHardness(0.6f);
        setWindDirection(speed);
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public Vector3f getMovement(Vector3f position) {
        return speed;
    }

}
