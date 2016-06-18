package com.navid.trafalgar.model;

import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.input.CommandBuilder;

import java.util.Set;

public interface AShipModelInteractive {

    Set<Command> getCommands(CommandBuilder commandBuilder);

    void setTarget(AShipModelPlayer ship);

}
