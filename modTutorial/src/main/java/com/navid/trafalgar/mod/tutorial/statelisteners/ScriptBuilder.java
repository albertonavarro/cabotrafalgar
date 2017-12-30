package com.navid.trafalgar.mod.tutorial.statelisteners;

import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.mod.tutorial.script.ScriptEvent;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;

import java.util.List;

/**
 * Created by alberto on 17/04/16.
 */
public class ScriptBuilder {

    private ScriptInterpreter scriptInterpreter;

    private EventManager eventManager;
    private LoadCameraStateListener loadCameraStateListener;
    private List<ScriptEvent> script;


    public List<ScriptEvent> getScript() {

        script.stream().forEach(step->step.setEventManager(eventManager).setScriptInterpreter(scriptInterpreter));

        return script;
    }

    public ScriptBuilder withScriptInterpreter(ScriptInterpreter scriptInterpreter) {
        this.scriptInterpreter = scriptInterpreter;
        return this;
    }

    public ScriptBuilder withEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        return this;
    }

    public ScriptBuilder withLoadCameraStateListener(LoadCameraStateListener loadCameraStateListener) {
        this.loadCameraStateListener = loadCameraStateListener;
        return this;
    }

    public ScriptBuilder withScript(List<ScriptEvent> script) {
        this.script = script;
        return this;
    }
}
