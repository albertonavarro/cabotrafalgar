package com.navid.trafalgar.model;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.water.WaterFilter;

public final class TidalWater extends WaterFilter implements Dependent, IWater {
    
    private IWind wind;

    public TidalWater() {
        setWaveScale(0.003f);
        setMaxAmplitude(2f);
        setFoamExistence(new Vector3f(1f, 4, 0.5f));
        setNormalScale(0.5f);
        setRefractionConstant(0.25f);
        setRefractionStrength(0.2f);
        setFoamHardness(0.6f);
    }

    @Override
    public void resolveDependencies(GameModel gameModel) {
        this.wind = gameModel.getSingleByType(IWind.class);
        setWindDirection(wind.getWind().mult(-1));
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public Vector2f getMovement(Vector2f position) {
        return new Vector2f(1,0);
    }

}
