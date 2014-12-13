package com.navid.trafalgar.model;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 *
 */
public interface IWater {

    /**
     *
     * @return
     */
    float getHeight();

    Vector2f getMovement(Vector2f position);
}
