package com.navid.trafalgar.input;

import com.navid.trafalgar.model.AShipModelInteractive;
import com.navid.trafalgar.model.AShipModelPlayer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alberto on 08/05/16.
 */
public class SystemInteractions implements AShipModelInteractive {

    private SystemInterpreter systemInteractionsPlayer;

    @Override
    public Set<Command> getCommands(final CommandBuilder commandBuilder) {
        return new HashSet<Command>() {
            {
                add(commandBuilder.createCommand("system - show menu", new Command() {
                    @Override
                    public void execute(float tpf) {
                        systemInteractionsPlayer.showMenu();
                    }
                }));
                add(commandBuilder.createCommand("system - show controls", new Command() {
                    @Override
                    public void execute(float tpf) {
                        systemInteractionsPlayer.showControls();
                    }
                }));
            }
        };
    }

    @Override
    public void setTarget(AShipModelPlayer ship) {
        this.systemInteractionsPlayer = (SystemInterpreter) ship;
    }
}
