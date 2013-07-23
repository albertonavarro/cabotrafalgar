/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.input;

import java.util.Set;

/**
 *
 * @author alberto
 */
public interface CommandGenerator {

    Set<Class<Command>> getPossibleCommands();

    CommandStateListener generateCommandStateListener(Command key);
}
