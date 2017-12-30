package com.navid.trafalgar.mod.tutorial.chapters;

import com.navid.trafalgar.mod.tutorial.CommandWrapper;
import com.navid.trafalgar.mod.tutorial.AIWrapper;
import com.navid.trafalgar.mod.tutorial.KeyboardWrapper;
import com.navid.trafalgar.mod.tutorial.TutorialChapter;
import com.navid.trafalgar.mod.tutorial.script.ScriptEvent;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alberto on 13/04/16.
 */
public class Chapter2 implements TutorialChapter {

    @Override
    public String toString() {
        return "02 - Sail Basics";
    }

    @Override
    public String getDescription() {
        return "Bring in and let go mainsail.";
    }

    @Override
    public String getShip() {
        return "ShipModelOneY";
    }

    @Override
    public String getMap() {
        return "mod.tutorial.chapter2";
    }

    @Override
    public Map<String, CommandWrapper> getCommandAssociations() {
        Map<String, CommandWrapper> map = new HashMap<String, CommandWrapper>();
        map.put("tiller - to port", new AIWrapper());
        map.put("tiller - to starboard", new AIWrapper());
        map.put("mainsail - bring in", new KeyboardWrapper());
        map.put("mainsail - let go", new KeyboardWrapper());
        return map;
    }

    @Override
    public List<ScriptEvent> getScript() {
        return null;
    }
}
