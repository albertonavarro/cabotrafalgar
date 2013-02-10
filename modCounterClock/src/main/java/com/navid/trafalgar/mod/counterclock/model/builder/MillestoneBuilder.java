package com.navid.trafalgar.mod.counterclock.model.builder;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.Builder2.Category;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.mod.counterclock.model.MillestoneModel;
import com.navid.trafalgar.util.FormatUtils;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class MillestoneBuilder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;
    @Autowired
    private EventManager eventManager;

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
     * @param eventManager the eventManager to set
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public Object build(String instanceName, Map<String, String> customValues) {
        MillestoneModel millestone = new MillestoneModel(assetManager);
        
        millestone.setMaterialOn(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"){{
            setColor("Color", ColorRGBA.Magenta);
        }});
        
        millestone.setMaterialOff(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"){{
            setColor("Color", ColorRGBA.Red);
        }});
        
        if(customValues.containsKey("position")){
            millestone.setLocalTranslation(FormatUtils.getVector3fFromString(customValues.get("position")));
        }
        
        return millestone;
    }

    public String getType() {
        return "Millestone";
    }
    
}
