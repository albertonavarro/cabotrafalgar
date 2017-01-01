package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.input.SystemInteractions;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.FanWind;
import com.navid.trafalgar.model.FanWindInteractive;
import com.navid.trafalgar.model.ModelBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

/**
 * Created by alberto on 28/12/16.
 */
public class SystemBuilder implements BuilderInterface {
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
        return emptyList();
    }

    @Override
    public Collection buildControls(String instanceName, Map<String, Object> customValues) {
        return singleton(new SystemInteractions());
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
        return "System";
    }

    @Override
    public Iterable<ModelBuilder.Category> getCategories() {
        return Collections.singleton(ModelBuilder.Category.other);
    }

}
