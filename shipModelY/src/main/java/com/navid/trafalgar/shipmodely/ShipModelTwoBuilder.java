package com.navid.trafalgar.shipmodely;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.ModelBuilder;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.util.FormatUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.singleton;

public final class ShipModelTwoBuilder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;

    @Autowired
    private EventManager eventManager;

    @Override
    public Collection buildGeometry(String instanceName, Map<String, Object> customValues) {
        AShipModelTwo model = new ShipModelTwoPlayer(assetManager, eventManager);
        model.setName(instanceName);
        if (customValues.containsKey("position")) {
            model.setLocalTranslation(FormatUtils.getVector3fFromString((String) customValues.get("position")));
        }
        return singleton(model);
    }

    @Override
    public Collection buildControls(String instanceName, Map<String, Object> customValues) {
        return singleton(new ShipModelTwoControlProxy());

    }

    @Override
    public Collection buildCandidateRecord(String instanceName, Map<String, Object> customValues) {
        return singleton(new ShipModelTwoPlayer.ShipCandidateRecord());
    }

    @Override
    public Collection buildGhost(String instanceName, Map<String, Object> customValues) {
        AShipModelTwo model = new ShipModelTwoGhost(assetManager, eventManager,
                (CandidateRecord<ShipModelTwoPlayer.ShipSnapshot>) customValues.get("record"));
        model.setName(instanceName);
        if (customValues.containsKey("position")) {
            model.setLocalTranslation(FormatUtils.getVector3fFromString((String) customValues.get("position")));
        }

        return singleton(model);
    }

    @Override
    public String getType() {
        return "ShipModelOneY";
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

    @Override
    public Iterable<ModelBuilder.Category> getCategories() {
        return singleton(ModelBuilder.Category.ship);
    }

}
