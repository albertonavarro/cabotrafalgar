package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.model.ModelBuilder.Category;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.SimpleContext;
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
public final class ContextBuilder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;

    @Override
    public Iterable<Category> getCategories() {
        return singleton(Category.context);
    }

    /**
     * @param assetManager the assetManager to set
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Collection buildGeometry(String instanceName, Map<String, Object> customValues) {
        return singleton(new SimpleContext());
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
        return "Context";
    }

}
