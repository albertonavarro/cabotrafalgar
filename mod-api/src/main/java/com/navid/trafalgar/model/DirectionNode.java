package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;

/**
 *
 * @author alberto
 */
public class DirectionNode extends Node {

    private Line line;
    

    public DirectionNode(Vector3f lookat, AssetManager assetManager) {
        line = new Line(new Vector3f(0, 0, 0), lookat.mult(20));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);

        Geometry lineGeometry = new Geometry("lineGeom", line);
        lineGeometry.setMaterial(mat);
        this.attachChild(lineGeometry);
    }

    public Vector3f getGlobalDirection() {
        Vector3f a = this.getWorldRotation().getRotationColumn(0);
        return a;
    }
    
    public Vector3f getLocalDirection() {
        Vector3f a = this.getLocalRotation().getRotationColumn(0);
        return a;
    }
    
    public void setDirection(Vector3f newdir){
        this.lookAt(newdir, Vector3f.UNIT_Y);
    }
}
