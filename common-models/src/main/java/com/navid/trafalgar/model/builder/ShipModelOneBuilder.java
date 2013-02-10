package com.navid.trafalgar.model.builder;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.Builder2.Category;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.ShipOneModel;
import com.navid.trafalgar.util.FormatUtils;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class ShipModelOneBuilder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;
    
    @Autowired
    private EventManager eventManager;

    

    public Iterable<Category> getCategories() {
        return Collections.singleton(Category.ship);
    }

    /**
     * @param assetManager the assetManager to set
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * @param eventManager the eventManager to set
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public Object build(String instanceName, Map<String, String> customValues) {
        ShipOneModel model = new ShipOneModel(assetManager, eventManager);
        
        model.setHullMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"){{
            setTexture("DiffuseMap", assetManager.loadTexture("Textures/wood.jpeg"));
        }});
        
        model.setSailMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"){{
            setTexture("DiffuseMap", assetManager.loadTexture("Textures/sail.jpg"));
        }});
        
        if(customValues.containsKey("position")){
            model.setLocalTranslation(FormatUtils.getVector3fFromString(customValues.get("position")));
        }
        
        return model;
    }

    public String getType() {
        return "ShipModelOneX";
    }
    
}
