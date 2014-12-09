/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.input;

import com.jme3.input.InputManager;
import java.util.Collections;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class KeyboardCommandGenerator implements CommandGenerator {

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

    /**
     * @param inputManager the inputManager to set
     */
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public String toString() {
        return "Keyboard";
    }

}
