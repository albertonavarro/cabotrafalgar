package com.navid.trafalgar.shipmodelz;

import com.jme3.asset.AssetManager;
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

public final class ShipModelZBuilder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;

    @Autowired
    private EventManager eventManager;

    @Override
    public Collection buildGeometry(String instanceName, Map<String, Object> customValues) {
        AShipModelZ model = new ShipModelZPlayer(assetManager, eventManager);
        model.setName(instanceName);
        if (customValues.containsKey("position")) {
            model.setLocalTranslation(FormatUtils.getVector3fFromString((String) customValues.get("position")));
        }
        return singleton(model);
    }

    @Override
    public Collection buildControls(String instanceName, Map<String, Object> customValues) {
        return singleton(new ShipModelZControlProxy());
    }

    @Override
    public Collection buildCandidateRecord(String instanceName, Map<String, Object> customValues) {
        return singleton(new ShipModelZPlayer.ShipCandidateRecord());
    }

    @Override
    public Collection buildGhost(String instanceName, Map<String, Object> customValues) {
        AShipModelZ model = new ShipModelZGhost(assetManager, eventManager,
                (CandidateRecord<ShipModelZPlayer.ShipSnapshot>) customValues.get("record"));
        model.setName(instanceName);
        if (customValues.containsKey("position")) {
            model.setLocalTranslation(FormatUtils.getVector3fFromString((String) customValues.get("position")));
        }
        return singleton(model);
    }

    @Override
    public String getType() {
        return "ShipModelOneZ";
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
