package com.navid.trafalgar.model;

import com.jme3.math.Vector3f;


public class NoWater implements IWater {
    
    private final Vector3f movement = new Vector3f();

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public Vector3f getMovement(Vector3f position) {
        return movement;
    }

}
