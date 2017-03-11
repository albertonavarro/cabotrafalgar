package com.navid.trafalgar.mod.tutorial.script;

/**
 * Created by alberto on 18/04/16.
 */
public interface ScriptInterpreter {

    void printMessage(String[] message);

    void printMessageNotSkippeable(String[] message);

    void cleanUpMessage();

}
