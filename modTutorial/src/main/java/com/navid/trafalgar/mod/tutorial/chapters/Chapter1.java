package com.navid.trafalgar.mod.tutorial.chapters;

import com.navid.trafalgar.mod.tutorial.*;

import java.util.HashMap;
import java.util.Map;

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
        map.put("tiller - to port", new KeyboardWrapper());
        map.put("tiller - to starboard", new KeyboardWrapper());
        map.put("mainsail - bring in", new AIWrapper());
        map.put("mainsail - let go", new AIWrapper());
        map.put("system - show menu", new KeyboardDigitalWrapper());
        map.put("system - show controls", new KeyboardDigitalWrapper());
        return map;
    }

}
