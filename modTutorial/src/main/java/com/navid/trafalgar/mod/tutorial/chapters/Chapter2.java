package com.navid.trafalgar.mod.tutorial.chapters;

import com.navid.trafalgar.mod.tutorial.CommandWrapper;
import com.navid.trafalgar.mod.tutorial.AIWrapper;
import com.navid.trafalgar.mod.tutorial.KeyboardWrapper;
import com.navid.trafalgar.mod.tutorial.TutorialChapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alberto on 13/04/16.
 */
public class Chapter2 implements TutorialChapter {

    @Override
    public String toString() {
        return "Chapter 2";
    }

    @Override
    public String getDescription() {
        return "Get used to the rudder.";
    }

    @Override
    public String getShip() {
        return "ShipModelOneY";
    }

    @Override
    public String getMap() {
        return "mod.tutorial.chapter1";
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
}
