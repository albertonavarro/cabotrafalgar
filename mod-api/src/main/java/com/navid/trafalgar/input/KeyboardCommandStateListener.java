/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.input;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.navid.trafalgar.manager.PrestartState;

/**
 *
 * @author alberto
 */
public class KeyboardCommandStateListener implements CommandStateListener, PrestartState {

    private InputManager inputManager;
    private AnalogListener listener;
    private int keycode;
    private Command key;

    public KeyboardCommandStateListener(InputManager inputManager, Command key) {
        this.inputManager = inputManager;
        this.key = key;
    }

    public void setKeycode(int keycode) {
        this.keycode = keycode;
    }
    
    public int getKeycode(){
        return keycode;
    }

    @Override
    public void onUnload() {
        inputManager.removeListener(listener);
        inputManager.deleteMapping(key.toString());
    }

    @Override
    public void onPrestart(float tpf) {
        listener = new AnalogListener() {
            @Override
            public void onAnalog(String string, float f, float f1) {
                key.execute(f);
            }
        };

        inputManager.addListener(listener, key.toString());
        inputManager.addMapping(key.toString(), new KeyTrigger(keycode));
    }

    @Override
    public String toString() {
        return key.toString();
    }
}
