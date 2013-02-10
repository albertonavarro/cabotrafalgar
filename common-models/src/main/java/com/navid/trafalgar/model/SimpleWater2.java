package com.navid.trafalgar.model;

import com.jme3.math.Vector3f;
import com.jme3.water.WaterFilter;

/**
 *
 * @author alberto
 */
public class SimpleWater2 extends WaterFilter{
    
    public SimpleWater2(){
        setWaveScale(0.003f);
        setMaxAmplitude(2f);
        setFoamExistence(new Vector3f(1f, 4, 0.5f));
        setNormalScale(0.5f);
        setRefractionConstant(0.25f);
        setRefractionStrength(0.2f);
        setFoamHardness(0.6f);
    }

}
