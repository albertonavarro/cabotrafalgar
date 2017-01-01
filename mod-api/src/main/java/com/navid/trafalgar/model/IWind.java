package com.navid.trafalgar.model;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 *
 *
 */
public abstract class IWind extends Node implements Control, Dependent {

    public abstract Vector2f getWind();

    @Override
    public final Control cloneForSpatial(Spatial spatial) {
        return null;
    }

    @Override
    public final void setSpatial(Spatial spatial) {

    }

    @Override
    public final void render(RenderManager rm, ViewPort vp) {

    }

}
