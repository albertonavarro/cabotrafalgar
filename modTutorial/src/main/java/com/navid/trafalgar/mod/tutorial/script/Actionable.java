package com.navid.trafalgar.mod.tutorial.script;

import com.navid.trafalgar.manager.EventManager;

/**
 * Created by alberto on 20/04/16.
 */
public abstract class Actionable {

    final protected ScriptInterpreter scriptInterpreter;

    final protected EventManager eventManager;

    public Actionable(ScriptInterpreter scriptInterpreter, EventManager eventManager) {
        this.scriptInterpreter = scriptInterpreter;
        this.eventManager = eventManager;
    }


    public abstract void action();

    public abstract void cleanUpAction();


}
