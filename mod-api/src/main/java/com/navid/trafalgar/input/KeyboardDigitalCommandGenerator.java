package com.navid.trafalgar.input;

import com.jme3.input.InputManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class KeyboardDigitalCommandGenerator implements CommandGenerator {

    @Autowired
    private InputManager inputManager;

    private Map<String, KeyboardDigitalCommandStateListener> report = new HashMap<String, KeyboardDigitalCommandStateListener>();

    @Override
    public Set<Class<Command>> getPossibleCommands() {
        return Collections.singleton(Command.class);
    }

    @Override
    public CommandStateListener generateCommandStateListener(final Command key) {
        KeyboardDigitalCommandStateListener generated = new KeyboardDigitalCommandStateListener(inputManager, key);
        report.put(key.toString(), generated);
        return generated;
    }

    @Override
    public Map<String, String> commandReport() {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, KeyboardDigitalCommandStateListener> entry: report.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getKeyname());
        }
        return result;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public String toString() {
        return "Keyboard-Digital";
    }

}
