package com.navid.trafalgar.input;

import com.google.common.base.Functions;
import com.navid.trafalgar.manager.EventManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;

/**
 * This class automates the event triggering in a command execution and the toString monotony.
 */
public class CommandBuilder {

    @Autowired
    private EventManager eventManager;

    public Command createCommand(final String commandName, final Command innerCommand) {
        return new Command() {
            @Override
            public void execute(float tpf) {
                eventManager.fireEvent("pre-" + commandName);
                innerCommand.execute(tpf);
                eventManager.fireEvent("post-" + commandName);
            }

            @Override
            public String toString() {
                return commandName;
            }
        };
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
}
