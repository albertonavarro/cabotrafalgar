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
 * This class extends node with some basic manipulation and debug information
 * - Set new direction for a node, rotating it automatically
 * - Gets local and global direction
 * - Displays an arrow with node direction in DEBUG mode
 */
abstract class TrafalgarNode extends Node {
    
    private boolean debug = false;
    private Geometry lineGeometry;
    protected EventManager eventManager;
    
    /**
     * Constructor for Trafalgar node
     * 
     * @param lookAt Initial director for the vector
     * @param assetManager necessary for creating vector material 
     * @param eventManager necessary for listening for DEBUG event
     */
    public TrafalgarNode(Vector3f lookAt, AssetManager assetManager, EventManager eventManager) {
        
        this.eventManager = eventManager;
        
        Line line = new Line(new Vector3f(0, 0, 0), lookAt.mult(20));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);

        lineGeometry = new Geometry("lineGeom", line);
        lineGeometry.setMaterial(mat);
        this.attachChild(lineGeometry);
        
        onDebugDisabled();
        
        eventManager.registerListener(new EventListener() {
            @Override
            public void onEvent(String event) {
                setDebug(!debug);
            }
        }, new String[]{"DEBUG"});
    }
    
    /**
     * Moves this node towards a new direction
     * @param newDirection 
     */
    public void setDirection(Vector3f newDirection) {
        this.lookAt(newDirection, Vector3f.UNIT_Y);
    }
    
    /**
     * Gets the Vector3f where the node is pointing to (local)
     * @return Vector3f with the direction
     */
    public Vector3f getLocalDirection() {
        return this.getLocalRotation().getRotationColumn(0);
    }
    
    /**
     * Get the Vector3f where the node is pointing to (global)
     * @return Vector3f with the direction
     */
    public Vector3f getGlobalDirection() {
        return this.getWorldRotation().getRotationColumn(0);
    }
    
    /**
     * Set debug mode for this node
     * @param value new debug mode
     */
    public final void setDebug(boolean value) {
        if (debug && !value) {
            onDebugEnabled();
        } else if (!debug && value) {
            onDebugDisabled();
        }
        this.debug = value;
    }
    
    /**
     * This method puts lineGeometry element in a render mode
     */
    final protected void onDebugEnabled() {
        lineGeometry.setCullHint(CullHint.Never);
    }
    
    /**
     * This method puts lineGeometry element in a non-render mode
     */
    final protected void onDebugDisabled() {
        lineGeometry.setCullHint(CullHint.Always);
    }
}
