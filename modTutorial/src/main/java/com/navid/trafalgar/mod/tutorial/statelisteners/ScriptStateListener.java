package com.navid.trafalgar.mod.tutorial.statelisteners;

import com.google.common.base.Optional;
import com.navid.trafalgar.manager.*;
import com.navid.trafalgar.mod.tutorial.NavigationScreenController;
import com.navid.trafalgar.mod.tutorial.script.ScriptEvent;
import com.navid.trafalgar.mod.tutorial.script.chapter1.ScriptBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by alberto on 17/04/16.
 */
public class ScriptStateListener implements InitState, PrestartState, StartedState {

    private ScriptEvent scriptEvent = null;

    private List<ScriptEvent> script = newArrayList();

    private int scriptIndex = 0;

    @Autowired
    private NavigationScreenController navigationScreenController;

    @Autowired
    private EventManager eventManager;

    @Override
    public void onInit(float tpf) {
        script = new ScriptBuilder().withScriptInterpreter(navigationScreenController).withEventManager(eventManager).getScript();
    }

    @Override
    public void onUnload() {

    }

    public void setNavigationScreenController(NavigationScreenController navigationScreenController) {
        this.navigationScreenController = navigationScreenController;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }


    @Override
    public void onPrestart(float tpf) {
        Optional<ScriptEvent> nextScript = getNextScript();

        if(!nextScript.isPresent()) {
            eventManager.fireEvent(EventManager.SUCCESSFUL);
            return;
        }

        processNextScript(nextScript.get());
    }

    private void processNextScript(final ScriptEvent scriptEvent) {
        //time to load a new event
        scriptEvent.getTrigger().register(scriptEvent.getAction());

        eventManager.registerListener(new EventListener() {
            @Override
            public void onEvent(String event) {
                scriptEvent.getAction().cleanUpAction();
                scriptEvent.getTrigger().unregister();
                eventManager.unregister(this);
            }
        }, new String[]{"SCRIPT_STEP_ACTIONED"});


    }

    private Optional<ScriptEvent> getNextScript() {
        if(script.size() <= scriptIndex) {
            return Optional.absent();
        }

        return Optional.of(script.get(scriptIndex++));
    }

    @Override
    public void onStarted(float tpf) {
        eventManager.fireEvent("FRAME_STEP");
    }
}
