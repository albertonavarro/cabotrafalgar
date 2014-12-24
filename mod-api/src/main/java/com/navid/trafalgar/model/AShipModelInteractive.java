package com.navid.trafalgar.model;

import com.navid.trafalgar.input.Command;
import java.util.Set;

public interface AShipModelInteractive {

    Set<Command> getCommands();

    void setTarget(AShipModelPlayer ship);

}
