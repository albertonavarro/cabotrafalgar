package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.Builder2.Category;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.SimpleContext;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class ContextBuilder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;

    @Override
    public Iterable<Category> getCategories() {
        return Collections.singleton(Category.context);
    }

    /**
     * @param assetManager the assetManager to set
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Collection build(String instanceName, Map<String, String> customValues) {
        return Collections.singleton(new SimpleContext());
    }

    @Override
    public String getType() {
        return "Context";
    }
    
}
