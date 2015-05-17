package com.navid.trafalgar.model;

import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public final class SunModel extends DirectionalLight {

    public SunModel(Vector3f vector3f, ColorRGBA colorRGBA) {
        super();
        this.setDirection(vector3f);
        this.setColor(colorRGBA);
    }

}
