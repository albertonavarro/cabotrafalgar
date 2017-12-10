package com.navid.trafalgar.mod.tutorial.script.chapter1;

import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.mod.tutorial.script.ScriptEvent;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;
import com.navid.trafalgar.mod.tutorial.script.action.EventActionable;
import com.navid.trafalgar.mod.tutorial.script.action.MessageActionable;
import com.navid.trafalgar.mod.tutorial.script.action.MessageNotSkippeableActionable;
import com.navid.trafalgar.mod.tutorial.script.trigger.EventTrigger;
import com.navid.trafalgar.mod.tutorial.script.trigger.TimeSinceLastEvent;
import com.navid.trafalgar.mod.tutorial.statelisteners.LoadCameraStateListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by alberto on 17/04/16.
 */
public class ScriptBuilder {

    private ScriptInterpreter scriptInterpreter;

    private EventManager eventManager;
    private LoadCameraStateListener loadCameraStateListener;


    public List<ScriptEvent> getScript() {

        List<ScriptEvent> scriptEvents = new ArrayList<ScriptEvent>();

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "Welcome to chapter 1 of this tutorial."}));
            setTrigger(new TimeSinceLastEvent(eventManager, 3000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "Hopefully thi is so basic that you won't need to replay it ever."}));
            setTrigger(new TimeSinceLastEvent(eventManager, 2000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new EventActionable(scriptInterpreter, eventManager, new String[] {"DEACTIVATE_CAM", "ACTIVATE_CAM2"}));
            setTrigger(new EventTrigger(eventManager, "FRAME_STEP"));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "Your first lesson is to continue with the tutorial, click Continue"}));
            setTrigger(new TimeSinceLastEvent(eventManager, 2000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new EventActionable(scriptInterpreter, eventManager, new String[] {"DEACTIVATE_CAM", "ACTIVATE_CAM3"}));
            setTrigger(new TimeSinceLastEvent(eventManager, 2000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageNotSkippeableActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "See main menu clicking 0, inspect the commands keys from there. Whenever you feel ready, hide controls and resume game."}));
            setTrigger(new TimeSinceLastEvent(eventManager, 1000));
            setSuccessEvent(new String[]{"SHOW_MENU"});
            setTimeoutMillis(Optional.of(10000L));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageNotSkippeableActionable(
                            scriptInterpreter, eventManager,
                            new String[] {""}));
            setTrigger(new EventTrigger(eventManager, "SCRIPT_STEP_ACTIONED"));
            setSuccessEvent(new String[]{"HIDE_MENU"});
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "Those particles represent the direction and speed of wind, they are coming from behind you now."}));
            setTrigger(new TimeSinceLastEvent(eventManager, 1000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "In front of you, the milestone you need to hit. Wait until you hit it."}));
            setTrigger(new TimeSinceLastEvent(eventManager, 2000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "End of tutorial."}));
            setTrigger(new EventTrigger(eventManager, "MILLESTONE_REACHED"));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageNotSkippeableActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "Click 0 and leave tutorial."}));
            setTrigger(new TimeSinceLastEvent(eventManager, 2000));
        }});



        return scriptEvents;
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
}
