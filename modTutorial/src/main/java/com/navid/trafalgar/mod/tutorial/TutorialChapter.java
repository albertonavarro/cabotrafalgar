package com.navid.trafalgar.mod.tutorial;

import com.navid.trafalgar.mod.tutorial.script.ScriptEvent;
import com.navid.trafalgar.model.AShipModelInteractive;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface TutorialChapter {

    String toString();

    String getDescription();

    String getShip();

    String getMap();

    Map<String, CommandWrapper> getCommandAssociations();

    List<ScriptEvent> getScript();
}
