package com.navid.trafalgar.mod.tutorial.script.chapter1;

import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;
import com.navid.trafalgar.mod.tutorial.script.action.MessageActionable;
import com.navid.trafalgar.mod.tutorial.script.trigger.EventTrigger;
import com.navid.trafalgar.mod.tutorial.script.ScriptEvent;
import com.navid.trafalgar.mod.tutorial.script.trigger.TimeSinceLastEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Callable;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by alberto on 17/04/16.
 */
public class ScriptBuilder {

    private ScriptInterpreter scriptInterpreter;

    private EventManager eventManager;


    public List<ScriptEvent> getScript() {

        ScriptEvent event1 = new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                        scriptInterpreter, eventManager,
                        new String[] {
                                "Welcome to chapter 1 of this tutorial."}));

            setTrigger(new EventTrigger(eventManager, "FRAME_STEP"));
        }};

        ScriptEvent event2 = new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "Hopefully thi is so basic that you won't need to replay it ever."}));

            setTrigger(new EventTrigger(eventManager, "FRAME_STEP"));
        }};

        ScriptEvent event3 = new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "Your first lesson is to continue with the tutorial, click OK"}));

            setTrigger(new TimeSinceLastEvent(eventManager, 10000));
        }};

        ScriptEvent event4 = new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            scriptInterpreter, eventManager,
                            new String[] {
                                    "Thanks for invoking ESC."}));

            setTrigger(new EventTrigger(eventManager, "pre-tiller - to port"));
        }};

        return newArrayList(event1, event2, event3, event4);
    }

    public ScriptBuilder withScriptInterpreter(ScriptInterpreter scriptInterpreter) {
        this.scriptInterpreter = scriptInterpreter;
        return this;
    }

    public ScriptBuilder withEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        return this;
    }

}
