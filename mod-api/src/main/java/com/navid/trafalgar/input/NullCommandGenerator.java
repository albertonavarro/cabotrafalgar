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
public class NullCommandGenerator implements CommandGenerator {

    @Override
    public Set<Class<Command>> getPossibleCommands() {
        return Collections.singleton(Command.class);
    }

    @Override
    public CommandStateListener generateCommandStateListener(final Command key) {
        return new CommandStateListener (){ 

            @Override
            public void onUnload() {
            }
        
        };
    }
    
    @Override
    public String toString(){
        return "Null Generator";
    }
    
    
}
