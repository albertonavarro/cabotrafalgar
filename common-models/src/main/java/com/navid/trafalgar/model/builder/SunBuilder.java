package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.SunModel;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public final class SunBuilder implements BuilderInterface {

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
        return Collections.singleton(new SunModel(new Vector3f(-1, -1, -1), ColorRGBA.Yellow));
    }

    @Override
    public String getType() {
        return "Sun";
    }

    @Override
    public Iterable<Builder2.Category> getCategories() {
        return Collections.singleton(Builder2.Category.context);
    }

}
