package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.navid.trafalgar.model.ModelBuilder;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.SunModel;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

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
    public Collection buildGeometry(String instanceName, Map<String, Object> customValues) {
        return singleton(new SunModel(new Vector3f(1, -1, 1), ColorRGBA.Yellow));
    }

    @Override
    public Collection buildControls(String instanceName, Map<String, Object> customValues) {
        return emptyList();
    }

    @Override
    public Collection buildCandidateRecord(String instanceName, Map<String, Object> customValues) {
        return emptyList();
    }

    @Override
    public Collection buildGhost(String instanceName, Map<String, Object> customValues) {
        return emptyList();
    }

    @Override
    public String getType() {
        return "Sun";
    }

    @Override
    public Iterable<ModelBuilder.Category> getCategories() {
        return singleton(ModelBuilder.Category.context);
    }

}
