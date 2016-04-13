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
public class Chapter1 implements TutorialChapter {

    public String toString() {
        return "Chapter 1";
    }

    public String getDescription() {
        return "Get used to weight controls.";
    }

    @Override
    public String getShip() {
        return "ShipModelOneY";
    }

    public String getMap() {
        return "chapter1";
    }

    @Override
    public Map<String, CommandWrapper> getCommandAssociations() {
        Map<String, CommandWrapper> map = new HashMap<String, CommandWrapper>();
        map.put("tiller - to port", new KeyboardWrapper());
        map.put("tiller - to starboard", new KeyboardWrapper());
        map.put("mainsail - bring in", new AIWrapper());
        map.put("mainsail - let go", new AIWrapper());
        return map;
    }

}
