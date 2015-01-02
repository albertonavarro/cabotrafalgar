package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.model.ModelBuilder.Category;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.RotateSimpleWind;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public final class RotateSimpleWindBuilder implements BuilderInterface {

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
        return Collections.singleton(new RotateSimpleWind(assetManager));
    }

    @Override
    public String getType() {
        return "RotateSimpleWind";
    }

    @Override
    public Iterable<Category> getCategories() {
        return Collections.singleton(Category.context);
    }

}
