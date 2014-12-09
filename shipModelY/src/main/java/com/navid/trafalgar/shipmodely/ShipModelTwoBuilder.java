package com.navid.trafalgar.shipmodely;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.util.FormatUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class ShipModelTwoBuilder implements BuilderInterface {

    @Autowired
    private AssetManager assetManager;

    @Autowired
    private EventManager eventManager;

    @Override
    public Collection build(String instanceName, Map<String, Object> customValues) {
        AShipModelTwo model = null;

        String role = (String) customValues.get("role");
        if (role.equals("Player")) {
            model = new ShipModelTwoPlayer(assetManager, eventManager);
        } else if (role.equals("Ghost")) {
            model = new ShipModelTwoGhost(assetManager, eventManager, (CandidateRecord<ShipModelTwoPlayer.ShipSnapshot>) customValues.get("record"));
        } else if (role.equals("ControlProxy")) {
            return Collections.singleton(new ShipModelTwoControlProxy());
        } else if (role.equals("CandidateRecord")) {
            return Collections.singleton(new ShipModelTwoPlayer.ShipCandidateRecord());
        } else {
            throw new IllegalArgumentException("Illegal role: " + role);
        }

        model.setName(instanceName);

        model.setHullMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md") {
            {
                setTexture("DiffuseMap", assetManager.loadTexture("Textures/wood.jpeg"));
            }
        });

        model.setSailMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md") {
            {
                setTexture("DiffuseMap", assetManager.loadTexture("Textures/sail.jpg"));
            }
        });

        if (customValues.containsKey("position")) {
            model.setLocalTranslation(FormatUtils.getVector3fFromString((String) customValues.get("position")));
        }
        return Collections.singleton(model);

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
    public Iterable<Builder2.Category> getCategories() {
        return Collections.singleton(Builder2.Category.ship);
    }

}
