package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;
import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;

/**
 *
 *   
 */
public abstract class TrafalgarNode extends Node {
    
    private boolean debug = false;
    private Geometry lineGeometry;
    protected EventManager eventManager;
    
    public TrafalgarNode(Vector3f lookAt, AssetManager assetManager, EventManager eventManager) {
        
        this.eventManager = eventManager;
        //this.lookAt(lookAt, Vector3f.UNIT_Y);
        
        Line line = new Line(new Vector3f(0, 0, 0), lookAt.mult(20));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);

        lineGeometry = new Geometry("lineGeom", line);
        lineGeometry.setMaterial(mat);
        this.attachChild(lineGeometry);
        
        onDebugDisabled();
        
        eventManager.registerListener(new EventListener() {
            public void onEvent(String event) {
                setDebug(!debug);
            }
        }, new String[]{"DEBUG"});
    }
    
    public void setDirection(Vector3f newDirection) {
        this.lookAt(newDirection, Vector3f.UNIT_Y);
    }
    
    public Vector3f getLocalDirection() {
        return this.getLocalRotation().getRotationColumn(0);
    }
    
    public Vector3f getGlobalDirection() {
        return this.getWorldRotation().getRotationColumn(0);
    }
    
    public final void setDebug(boolean value) {
        if (debug && !value) {
            onDebugEnabled();
        } else if (!debug && value) {
            onDebugDisabled();
        }
        this.debug = value;
    }
    
    protected void onDebugEnabled() {
        lineGeometry.setCullHint(CullHint.Never);
    }
    
    protected void onDebugDisabled() {
        lineGeometry.setCullHint(CullHint.Always);
    }
}
