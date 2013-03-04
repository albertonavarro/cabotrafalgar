package com.navid.trafalgar.mod.windtunnel.model.builder;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.mod.windtunnel.model.HarnessModel;
import com.navid.trafalgar.model.Builder2.Category;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.Dependent;
import com.navid.trafalgar.model.GameModel;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class HarnessBuilder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;
    
    @Override
    public Object build(String instanceName, Map<String, String> customValues) {
        return new HarnessModel(assetManager);
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

}
