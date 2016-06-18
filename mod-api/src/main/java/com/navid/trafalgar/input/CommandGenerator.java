package com.navid.trafalgar.input;

import java.util.Map;
import java.util.Set;

public interface CommandGenerator {

    Set<Class<Command>> getPossibleCommands();

    CommandStateListener generateCommandStateListener(Command key);

    Map<String, String> commandReport();
}
