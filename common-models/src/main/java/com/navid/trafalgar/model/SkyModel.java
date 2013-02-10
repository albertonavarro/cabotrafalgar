package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.util.Map;

/**
 *
 * @author alberto
 */
public class SkyModel extends Node {
    
    public SkyModel( AssetManager assetManager, Texture texture, boolean isSpheric){
    
        Spatial sky = SkyFactory.createSky(assetManager, texture, isSpheric);
            sky.setLocalScale(350);
            this.attachChild(sky);
            
    }
    
    
    
}
