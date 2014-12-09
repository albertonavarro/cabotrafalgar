package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.Arrow;

/**
 *
 *
 */
public class ArrowModel extends Geometry {

    /**
     *
     * @param assetManager
     * @param orientation
     */
    public ArrowModel(AssetManager assetManager, Vector3f orientation) {
        super("arrow", new Arrow(Vector3f.UNIT_Z));

        this.lookAt(orientation, Vector3f.UNIT_Y);

        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        this.setMaterial(mat);

    }

}
