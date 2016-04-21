package com.navid.trafalgar.mod.tutorial.script.action;

import com.navid.trafalgar.mod.tutorial.script.Actionable;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;

public class MessageActionable extends Actionable {

    private final String[] messages;

    public MessageActionable(ScriptInterpreter scriptInterpreter, String[] messages) {
        super(scriptInterpreter);
        this.messages = messages;
    }

    @Override
    public void action() {
        scriptInterpreter.printMessage(messages);
    }

    @Override
    public void cleanUpAction() {
        scriptInterpreter.cleanUpMessage();
    }

}
