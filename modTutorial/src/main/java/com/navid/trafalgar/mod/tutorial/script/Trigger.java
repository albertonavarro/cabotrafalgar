package com.navid.trafalgar.mod.tutorial.script;

import com.navid.trafalgar.manager.EventManager;

import java.util.concurrent.Callable;

/**
 * Created by alberto on 17/04/16.
 */
public interface Trigger {

    void register(final Actionable actionable);

    void unregister();

    void setEventManager(EventManager eventManager);

    void setScriptInterpreter(ScriptInterpreter scriptInterpreter);
}
