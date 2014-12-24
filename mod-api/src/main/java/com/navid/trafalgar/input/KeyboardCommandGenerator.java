package com.navid.trafalgar.input;

import com.jme3.input.InputManager;
import java.util.Collections;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

public final class KeyboardCommandGenerator implements CommandGenerator {

    @Autowired
    private InputManager inputManager;

    @Override
    public Set<Class<Command>> getPossibleCommands() {
        return Collections.singleton(Command.class);
    }

    @Override
    public CommandStateListener generateCommandStateListener(final Command key) {
        return new KeyboardCommandStateListener(inputManager, key);
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public String toString() {
        return "Keyboard";
    }

}
