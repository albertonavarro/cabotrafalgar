package com.navid.trafalgar.mod.tutorial.script.action;

import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.mod.tutorial.script.Actionable;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;

public class MessageActionable extends Actionable {

    private final String[] messages;

    private final boolean withPause;

    public MessageActionable(ScriptInterpreter scriptInterpreter, EventManager eventManager, String[] messages) {
        super(scriptInterpreter, eventManager);
        this.messages = messages;
        withPause = true;
    }

    public MessageActionable(ScriptInterpreter scriptInterpreter, EventManager eventManager, String[] messages, boolean pause) {
        super(scriptInterpreter, eventManager);
        this.messages = messages;
        withPause = pause;
    }

    @Override
    public void action() {
        scriptInterpreter.printMessage(messages);
        if (withPause) {
            eventManager.fireEvent("PAUSE");
        }
    }

    @Override
    public void cleanUpAction() {
        scriptInterpreter.cleanUpMessage();
    }


}
