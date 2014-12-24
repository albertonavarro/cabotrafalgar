package com.navid.trafalgar.mod.windtunnel.model;

import com.jme3.asset.AssetManager;

/**
 *
 * @author alberto
 */
public final class HarnessModel extends AHarnessModel {

    public HarnessModel(AssetManager assetManager) {
        super(assetManager);
    }

    @Override
    protected void initGeometry(AssetManager assetManager) {

    }

    public boolean isEnabled() {
        return true;
    }

    public void setEnabled(boolean value) {
        //todo
    }
}
