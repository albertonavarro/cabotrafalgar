package com.navid.trafalgar.mod.windtunnel.statelisteners;

import com.jme3.app.Application;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.navid.trafalgar.manager.InitState;

/**
 *
 * @author anf
 */
public class LoadEventsListener implements InitState {

    private InputManager inputManager;

    public LoadEventsListener(Application app) {
        inputManager = app.getInputManager();

    }

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
        inputManager.addMapping("DEBUG", new KeyTrigger(KeyInput.KEY_Z));
        
        inputManager.addMapping("WindTunnel_RotateRight", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("WindTunnel_RotateLeft", new KeyTrigger(KeyInput.KEY_K));
    }

    public void onUnload() {
        inputManager.clearMappings();
    }
    
    
    
}
