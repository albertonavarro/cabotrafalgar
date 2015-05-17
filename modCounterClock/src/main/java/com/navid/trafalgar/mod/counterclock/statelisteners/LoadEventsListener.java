package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.navid.trafalgar.manager.InitState;
import org.springframework.beans.factory.annotation.Autowired;

public final class LoadEventsListener implements InitState {

    @Autowired
    private InputManager inputManager;

    @Override
    public void onInit(float tpf) {
        inputManager.addMapping("SHIP_RudderRight", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("SHIP_RudderLeft", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("SHIP_SailRight", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("SHIP_SailLeft", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping("SHIP_SailTrim", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("SHIP_SailLoose", new KeyTrigger(KeyInput.KEY_G));

        inputManager.addMapping("Cam1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Cam2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Cam3", new KeyTrigger(KeyInput.KEY_3));

        inputManager.addMapping("Menu", new KeyTrigger(KeyInput.KEY_ESCAPE));
    }

    @Override
    public void onUnload() {
        inputManager.clearMappings();
    }

    /**
     * @param inputManager the inputManager to set
     */
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }
}
