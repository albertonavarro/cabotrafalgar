package com.navid.trafalgar.input;

import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.navid.trafalgar.manager.PrestartState;

public class KeyboardCommandStateListener implements CommandStateListener, PrestartState {

    protected final InputManager inputManager;
    protected final Command key;
    private AnalogListener listener;
    private String keyname;
    private int keycode;

    public KeyboardCommandStateListener(InputManager inputManager, Command key) {
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
