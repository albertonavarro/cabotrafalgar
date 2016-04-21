package com.navid.trafalgar.mod.tutorial.script;

/**
 * Created by alberto on 20/04/16.
 */
public abstract class Actionable {

    public Actionable(ScriptInterpreter scriptInterpreter) {
        this.scriptInterpreter = scriptInterpreter;
    }

    final protected ScriptInterpreter scriptInterpreter;

    public abstract void action();

    public abstract void cleanUpAction();


}
