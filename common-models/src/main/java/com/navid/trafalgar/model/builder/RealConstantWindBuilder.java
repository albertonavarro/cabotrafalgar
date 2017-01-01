package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.ModelBuilder.Category;
import com.navid.trafalgar.model.RealConstantWind;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

public final class RealConstantWindBuilder implements BuilderInterface {

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
        return singleton(new RealConstantWind(assetManager));
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
        return "RotateSimpleWind";
    }

    @Override
    public Iterable<Category> getCategories() {
        return singleton(Category.context);
    }

}
