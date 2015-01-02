package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.navid.trafalgar.model.ModelBuilder;
import com.navid.trafalgar.model.BuilderInterface;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public final class SkyBuilder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;

    /**
     * @param assetManager the assetManager to set
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Collection build(String instanceName, Map<String, Object> customValues) {
        Spatial sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false);
        return Collections.singleton(sky);
    }

    @Override
    public String getType() {
        return "Sky";
    }

    @Override
    public Iterable<ModelBuilder.Category> getCategories() {
        return Collections.singleton(ModelBuilder.Category.context);
    }
}
