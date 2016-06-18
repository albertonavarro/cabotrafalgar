package com.navid.trafalgar.input;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.navid.trafalgar.util.ReflexionUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GeneratorBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(GeneratorBuilder.class);
    private final HashMultimap<Class<Command>, CommandGenerator> generatorMap = HashMultimap.create();
    private final Map<String, CommandGenerator> generatorOnlyMap = new HashMap<String, CommandGenerator>();

    public void registerBuilder(CommandGenerator commandGenerator) {
        LOG.info("Registring builder " + commandGenerator);
        generatorOnlyMap.put(commandGenerator.toString(), commandGenerator);

        Set<Class<Command>> currentClasses = commandGenerator.getPossibleCommands();

        for (Class<Command> currentClass : currentClasses) {
            generatorMap.put(currentClass, commandGenerator);
            for (Class finalClass : ReflexionUtils.getSuperTypes(currentClass)) {
                generatorMap.put(finalClass, commandGenerator);
            }
        }
    }

    public Map<String, CommandGenerator> getGenerators() {
        return generatorOnlyMap;
    }

    public Multimap<Command, CommandGenerator> getGeneratorsFor(Set<Command> commands) {

        HashMultimap<Command, CommandGenerator> result = HashMultimap.create();

        for (Command currentCommand : commands) {
            for (Class commandClasses : ReflexionUtils.getSuperTypes(currentCommand)) {
                result.putAll(currentCommand, generatorMap.get(commandClasses));
            }
        }

        return result;
    }

    public Set<CommandStateListener> generateControllers(Map<Command, CommandGenerator> assignments) {

        Set<CommandStateListener> commandStateListeners = new HashSet<CommandStateListener>();

        for (Map.Entry<Command, CommandGenerator> entry : assignments.entrySet()) {
            if(entry.getValue() != null) {
                commandStateListeners.add(entry.getValue().generateCommandStateListener(entry.getKey()));
            } else {
                LOG.error("Unknown generator for entry {}", entry.getKey());
            }
        }

        return commandStateListeners;
    }

    public Map<String, String> generateReport() {
        Map<String, String> result = new HashMap<>();

        for(CommandGenerator cg : generatorOnlyMap.values()) {
            result.putAll(cg.commandReport());
        }

        return result;
    }

}
