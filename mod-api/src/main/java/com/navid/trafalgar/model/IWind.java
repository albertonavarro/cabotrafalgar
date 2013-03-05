package com.navid.trafalgar.model;

import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;

/**
 *
 *   
 */
public interface IWind {
    
    /**
     *
     * @return
     */
    Vector2f getWind();
    
    /**
     *
     * @return
     */
    Geometry getGeometry();
    
    /**
     *
     * @return
     */
    WindGeometry createWindGeometryNode();
    
    /**
     *
     */
    public  abstract class WindGeometry extends Node implements Control{}
    
}
