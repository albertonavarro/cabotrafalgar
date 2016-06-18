package com.navid.trafalgar.input;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.navid.trafalgar.manager.PrestartState;

public final class KeyboardDigitalCommandStateListener implements CommandStateListener, PrestartState {

    private final InputManager inputManager;
    private final Command key;
    private ActionListener listener;
    private String keyname;
    private int keycode;

    public KeyboardDigitalCommandStateListener(InputManager inputManager, Command key) {
        this.inputManager = inputManager;
        this.key = key;
    }

    public void setKeycode(String keyname, int keycode) {
        this.keyname = keyname;
        this.keycode = keycode;
    }

    public int getKeycode() {
        return keycode;
    }

    public String getKeyname() {
        return keyname;
    }

    @Override
    public void onUnload() {
        inputManager.removeListener(listener);
        inputManager.deleteMapping(key.toString());
    }

    @Override
    public void onPrestart(float tpf) {
        listener = new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if(isPressed){
                    key.execute(tpf);
                }
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
