package com.navid.trafalgar.modwindtunnel.model;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.modwindtunnel.model.AHarnessModel;

/**
 *
 * @author alberto
 */
public class HarnessModel extends AHarnessModel {

    public HarnessModel(AssetManager assetManager) {
        super(assetManager);
    }

    @Override
    protected void initGeometry(AssetManager assetManager) {

    }
    
    public boolean isEnabled(){
        return true;
    }
    
    public void setEnabled(boolean value){
        //todo
    }
}
