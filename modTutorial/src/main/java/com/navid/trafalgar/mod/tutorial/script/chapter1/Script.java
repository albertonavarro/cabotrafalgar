package com.navid.trafalgar.mod.tutorial.script.chapter1;

import com.navid.trafalgar.mod.tutorial.script.EventTrigger;
import com.navid.trafalgar.mod.tutorial.script.ScriptEvent;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by alberto on 17/04/16.
 */
public class Script {

    private List<ScriptEvent> getScript() {

        ScriptEvent event1 = new ScriptEvent(){{
            setMessages(
                    newArrayList(
                            "Welcome to chapter 1 of this tutorial.",
                            "Hopefully thi is so basic that you won't need to replay it ever.",
                            "Your first lesson is to continue with the tutorial, click OK"));
            setTrigger(new EventTrigger("START_EVENT"));
        }};

        return newArrayList(event1);
    }

}
