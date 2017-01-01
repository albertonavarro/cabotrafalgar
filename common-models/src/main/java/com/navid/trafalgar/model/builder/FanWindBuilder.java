package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.model.FanWind;
import com.navid.trafalgar.model.FanWindInteractive;
import com.navid.trafalgar.model.ModelBuilder.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

public final class FanWindBuilder implements BuilderInterface {

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
        return singleton(new FanWind(assetManager));
    }

    @Override
    public Collection buildControls(String instanceName, Map<String, Object> customValues) {
        return singleton(new FanWindInteractive(null));
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
        return "FanWind";
    }

    @Override
    public Iterable<Category> getCategories() {
        return singleton(Category.context);
    }

}
