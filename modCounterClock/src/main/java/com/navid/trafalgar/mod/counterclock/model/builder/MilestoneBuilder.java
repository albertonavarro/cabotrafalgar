package com.navid.trafalgar.mod.counterclock.model.builder;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.navid.trafalgar.mod.counterclock.model.MilestoneModel;
import com.navid.trafalgar.model.ModelBuilder.Category;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.util.FormatUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

public final class MilestoneBuilder implements BuilderInterface {

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
        MilestoneModel milestone = new MilestoneModel(assetManager);

        milestone.setMaterialOn(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md") {
            {
                setColor("Color", ColorRGBA.Magenta);
            }
        });

        milestone.setMaterialOff(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md") {
            {
                setColor("Color", ColorRGBA.Red);
            }
        });

        if (customValues.containsKey("position")) {
            milestone.setLocalTranslation(FormatUtils.getVector3fFromString((String) customValues.get("position")));
        }

        return singleton(milestone);
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
        return "Milestone";
    }

}
