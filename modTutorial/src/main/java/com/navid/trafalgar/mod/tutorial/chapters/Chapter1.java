package com.navid.trafalgar.mod.tutorial.chapters;

import com.navid.trafalgar.mod.tutorial.*;
import com.navid.trafalgar.mod.tutorial.script.ScriptEvent;
import com.navid.trafalgar.mod.tutorial.script.action.EventActionable;
import com.navid.trafalgar.mod.tutorial.script.action.MessageActionable;
import com.navid.trafalgar.mod.tutorial.script.action.MessageNotSkippeableActionable;
import com.navid.trafalgar.mod.tutorial.script.template.ScriptEventTemplate;
import com.navid.trafalgar.mod.tutorial.script.trigger.EventTrigger;
import com.navid.trafalgar.mod.tutorial.script.trigger.TimeSinceLastEvent;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by alberto on 13/04/16.
 */
public class Chapter1 implements TutorialChapter {

    public String toString() {
        return "01 - Screen basics";
    }

    public String getDescription() {
        return "Learn how to run the tutorials, basic instructions, no gameplay.\n" +
                "Ship: ShipModelOneY\n" +
                "Choose and remember the keys for the commands!";
    }

    @Override
    public String getShip() {
        return "ShipModelOneY";
    }

    public String getMap() {
        return "mod/tutorial/chapter1/chapter1.map";
    }

    @Override
    public Map<String, CommandWrapper> getCommandAssociations() {
        Map<String, CommandWrapper> map = new HashMap<String, CommandWrapper>();
        map.put("tiller - to port", new AIWrapper());
        map.put("tiller - to starboard", new AIWrapper());
        map.put("mainsail - bring in", new AIWrapper());
        map.put("mainsail - let go", new AIWrapper());
        map.put("system - show menu", new KeyboardDigitalWrapper());
        return map;
    }

    @Override
    public List<ScriptEvent> getScript() {
        List<ScriptEvent> scriptEvents = new ArrayList<>();

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            new String[] {
                                    "Welcome to chapter 1 of this tutorial."}));
            setTrigger(new TimeSinceLastEvent(3000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            new String[] {
                                    "Hopefully thi is so basic that you won't need to replay it ever."}));
            setTrigger(new TimeSinceLastEvent(2000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new EventActionable(new String[] {"DEACTIVATE_CAM", "ACTIVATE_CAM2"}));
            setTrigger(new EventTrigger("FRAME_STEP"));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            new String[] {
                                    "Your first lesson is to continue with the tutorial, click Continue"}));
            setTrigger(new TimeSinceLastEvent( 2000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new EventActionable(new String[] {"DEACTIVATE_CAM", "ACTIVATE_CAM3"}));
            setTrigger(new TimeSinceLastEvent(2000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageNotSkippeableActionable(
                            new String[] {
                                    "See main menu clicking ::command::system - show menu::, inspect the commands keys from there. Whenever you feel ready, hide controls and resume game."}));
            setTrigger(new TimeSinceLastEvent(1000));
            setSuccessEvent(new String[]{"SHOW_MENU"});
            setTimeoutMillis(Optional.of(10000L));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageNotSkippeableActionable(
                            new String[] {""}));
            setTrigger(new EventTrigger("SCRIPT_STEP_ACTIONED"));
            setSuccessEvent(new String[]{"HIDE_MENU"});
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            new String[] {
                                    "Those particles represent the direction and speed of wind, they are coming from behind you now."}));
            setTrigger(new TimeSinceLastEvent( 1000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            new String[] {
                                    "In front of you, the milestone you need to hit. Wait until you hit it."}));
            setTrigger(new TimeSinceLastEvent( 2000));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageActionable(
                            new String[] {
                                    "End of tutorial."}));
            setTrigger(new EventTrigger( "MILLESTONE_REACHED"));
        }});

        scriptEvents.add(new ScriptEvent(){{
            setAction(
                    new MessageNotSkippeableActionable(
                            new String[] {
                                    "Click ::command::system - show menu:: and leave tutorial."}));
            setTrigger(new TimeSinceLastEvent( 2000));
        }});

        return scriptEvents;

    }

}
