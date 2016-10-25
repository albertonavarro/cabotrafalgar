package com.navid.trafalgar.input;

import com.jme3.input.InputManager;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

public final class KeyboardCommandGenerator implements CommandGenerator {

    @Autowired
    private InputManager inputManager;

    private Map<String, KeyboardCommandStateListener> report = new HashMap<String, KeyboardCommandStateListener>();

    @Override
    public Set<Class<? extends Command>> getPossibleCommands() {
        Set<Class<? extends Command>> result = new HashSet<>();
        result.add(AnalogCommand.class);
        return result;
    }

    @Override
    public CommandStateListener generateCommandStateListener(final Command key) {
        KeyboardCommandStateListener generated = new KeyboardCommandStateListener(inputManager, key);
        report.put(key.toString(), generated);
        return generated;
    }

    @Override
    public Map<String, String> commandReport() {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, KeyboardCommandStateListener> entry: report.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getKeyname());
        }
        return result;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public String toString() {
        return "Keyboard";
    }

}
