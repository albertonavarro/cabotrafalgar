package com.navid.trafalgar.model;

import com.google.common.collect.Sets;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.manager.EventManager;
import java.util.Set;

/**
 *
 * @author casa
 */
public class ShipModelTwoGhost extends AShipModelTwo {

    public ShipModelTwoGhost(String role, AssetManager assetManager, EventManager eventManager) {
        super(role, assetManager, eventManager);
    }

    @Override
    protected void initGeometry(AssetManager assetManager, EventManager eventManager) {

    }

    @Override
    public void registerInput(InputManager inputManager) {
    }

    @Override
    public Set<Command> getCommands() {
        return Sets.newHashSet();
    }

    @Override
    protected void initStatisticsManager() {
    }

    @Override
    public float getSpeed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
