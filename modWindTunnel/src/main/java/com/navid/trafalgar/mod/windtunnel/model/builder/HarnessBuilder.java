package com.navid.trafalgar.mod.windtunnel.model.builder;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.navid.trafalgar.mod.windtunnel.model.HarnessModel;
import com.navid.trafalgar.model.Builder2.Category;
import com.navid.trafalgar.model.BuilderInterface;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

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
    public Collection build(String instanceName, Map<String, Object> customValues) {
        HarnessModel harness = new HarnessModel(assetManager);
        harness.registerInputManager(inputManager);
        return Collections.singleton(harness);
    }

    @Override
    public String getType() {
        return "Harness";
    }

    @Override
    public Iterable<Category> getCategories() {
        return Collections.singleton(Category.other);
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
