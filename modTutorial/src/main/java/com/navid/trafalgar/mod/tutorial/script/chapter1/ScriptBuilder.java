package com.navid.trafalgar.mod.tutorial.script.chapter1;

import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;
import com.navid.trafalgar.mod.tutorial.script.action.MessageActionable;
import com.navid.trafalgar.mod.tutorial.script.trigger.EventTrigger;
import com.navid.trafalgar.mod.tutorial.script.ScriptEvent;
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
                        scriptInterpreter,
                        new String[] {
                                "Welcome to chapter 1 of this tutorial.",
                                "Hopefully thi is so basic that you won't need to replay it ever.",
                                "Your first lesson is to continue with the tutorial, click OK"}));

            setTrigger(new EventTrigger(eventManager, "FRAME_STEP"));
        }};

        return newArrayList(event1);
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
