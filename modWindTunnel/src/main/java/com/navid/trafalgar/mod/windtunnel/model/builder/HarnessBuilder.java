package com.navid.trafalgar.mod.windtunnel.model.builder;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.navid.trafalgar.mod.windtunnel.model.HarnessModel;
import com.navid.trafalgar.model.ModelBuilder.Category;
import com.navid.trafalgar.model.BuilderInterface;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

/**
 *
 * @author alberto
 */
public final class HarnessBuilder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;

    @Autowired
    private InputManager inputManager;

    @Override
    public Collection buildGeometry(String instanceName, Map<String, Object> customValues) {
        HarnessModel harness = new HarnessModel(assetManager);
        harness.registerInputManager(inputManager);
        return singleton(harness);
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
        return "Harness";
    }

    @Override
    public Iterable<Category> getCategories() {
        return singleton(Category.other);
    }

    /**
     * @param assetManager the assetManager to set
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * @param inputManager the inputManager to set
     */
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

}
