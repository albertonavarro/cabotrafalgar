/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.input;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.navid.trafalgar.manager.PrestartState;

/**
 *
 * @author alberto
 */
public class KeyboardCommandStateListener implements CommandStateListener, PrestartState {

    private InputManager inputManager;
    private ActionListener listener;
    private int keycode;
    private Command key;

    public KeyboardCommandStateListener(InputManager inputManager, Command key) {
        this.inputManager = inputManager;
        this.key = key;
    }

    public void setKeycode(int keycode) {
        this.keycode = keycode;
    }

    @Override
    public void onUnload() {
        inputManager.removeListener(listener);
        inputManager.deleteMapping(key.getName());
    }

    @Override
    public void onPrestart(float tpf) {
        listener = new ActionListener() {
            @Override
            public void onAction(String string, boolean bln, float tpf) {
                if (bln) {
                    key.execute(tpf);
                }
            }
        };

        inputManager.addListener(listener, key.getName());
        inputManager.addMapping(key.getName(), new KeyTrigger(keycode));
    }
    
    @Override
    public String toString(){
        return key.getName();
    }
}
