/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.input;

import com.google.common.collect.HashMultimap;
import com.navid.trafalgar.util.ReflexionUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
public class GeneratorBuilder {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(GeneratorBuilder.class);
    private HashMultimap<Class<Command>, CommandGenerator> generatorMap = HashMultimap.create();
    private Map<String, CommandGenerator> generatorOnlyMap = new HashMap<String, CommandGenerator>();

    public void registerBuilder(CommandGenerator commandGenerator) {
        LOG.info("Registring builder %s");
        generatorOnlyMap.put(commandGenerator.toString(), commandGenerator);

        Set<Class<Command>> currentClasses = commandGenerator.getPossibleCommands();

        for (Class<Command> currentClass : currentClasses) {
            generatorMap.put(currentClass, commandGenerator);
            for (Class finalClass : ReflexionUtils.getSuperTypes(currentClass)) {
                generatorMap.put(finalClass, commandGenerator);
            }
        }
    }
    
    public Map<String, CommandGenerator> getGenerators(){
        return generatorOnlyMap;
    }

    public HashMultimap<Command, CommandGenerator> getGeneratorsFor(Set<Command> commands) {

        HashMultimap<Command, CommandGenerator> result = HashMultimap.create();

        for (Command currentCommand : commands) {
            for( Class commandClasses : ReflexionUtils.getSuperTypes(currentCommand)){
                result.putAll(currentCommand, generatorMap.get(commandClasses));
            }
        }

        return result;
    }

    public Set<CommandStateListener> generateControllers(Map<Command, CommandGenerator> assignments) {

        Set<CommandStateListener> commandStateListeners = new HashSet<CommandStateListener>();

        for (Map.Entry<Command, CommandGenerator> entry : assignments.entrySet()) {
            commandStateListeners.add(entry.getValue().generateCommandStateListener(entry.getKey()));
        }

        return commandStateListeners;
    }

    
}
