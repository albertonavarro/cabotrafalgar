package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.SkyModel;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class SkyBuilder  implements BuilderInterface{
    
    @Autowired
    private AssetManager assetManager;
    
    /**
     * @param assetManager the assetManager to set
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Object build(String instanceName, Map<String, String> customValues) {
        assetManager.loadTexture("Scenes/Beach/FullskiesSunset0068.dds");
        
        return new SkyModel(assetManager, assetManager.loadTexture("Scenes/Beach/FullskiesSunset0068.dds"), false);
    }

    public String getType() {
        return "Sky";
    }

    public Iterable<Builder2.Category> getCategories() {
        return Collections.singleton(Builder2.Category.context);
    }
    
}
