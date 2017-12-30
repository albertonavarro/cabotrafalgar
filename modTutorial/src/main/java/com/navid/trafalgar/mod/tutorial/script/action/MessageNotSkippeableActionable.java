package com.navid.trafalgar.mod.tutorial.script.action;

import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.mod.tutorial.script.Actionable;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;

public class MessageNotSkippeableActionable extends Actionable {

    private final String[] messages;

    private final boolean withPause;

    public MessageNotSkippeableActionable(ScriptInterpreter scriptInterpreter, EventManager eventManager, String[] messages) {
        super(scriptInterpreter, eventManager);
        this.messages = messages;
        withPause = false;
    }

    public MessageNotSkippeableActionable(String[] messages) {
        super(null,null);
        this.messages = messages;
        withPause = false;
    }

    @Override
    public void action() {
        scriptInterpreter.printMessageNotSkippeable(messages);
    }

    @Override
    public void cleanUpAction() {
        scriptInterpreter.cleanUpMessage();
    }

}
