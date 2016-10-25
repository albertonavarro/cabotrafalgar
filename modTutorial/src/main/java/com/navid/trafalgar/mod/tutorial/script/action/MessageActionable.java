package com.navid.trafalgar.mod.tutorial.script.action;

import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.mod.tutorial.script.Actionable;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;

public class MessageActionable extends Actionable {

    private final String[] messages;

    public MessageActionable(ScriptInterpreter scriptInterpreter, EventManager eventManager, String[] messages) {
        super(scriptInterpreter, eventManager);
        this.messages = messages;
    }

    @Override
    public void action() {
        scriptInterpreter.printMessage(messages);
        eventManager.fireEvent("PAUSE");
    }

    @Override
    public void cleanUpAction() {
        scriptInterpreter.cleanUpMessage();
    }

}
