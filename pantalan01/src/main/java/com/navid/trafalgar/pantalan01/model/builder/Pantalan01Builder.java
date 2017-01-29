package com.navid.trafalgar.pantalan01.model.builder;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.ModelBuilder.Category;
import com.navid.trafalgar.pantalan01.model.Pantalan01Model;
import com.navid.trafalgar.util.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

public final class Pantalan01Builder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;

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

    @Override
    public Collection buildGeometry(String instanceName, Map<String, Object> customValues) {
        Pantalan01Model pantalan01Model = new Pantalan01Model(assetManager);

        if (customValues.containsKey("position")) {
            pantalan01Model.setLocalTranslation(FormatUtils.getVector3fFromString((String) customValues.get("position")));
        }

        return singleton(pantalan01Model);
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
        return "Pantalan01";
    }

}
