package com.navid.trafalgar.input;

import com.navid.trafalgar.model.AShipModelInteractive;
import com.navid.trafalgar.model.AShipModelPlayer;
import com.navid.trafalgar.model.Dependent;
import com.navid.trafalgar.model.GameModel;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alberto on 08/05/16.
 */
public class SystemInteractions implements AShipModelInteractive, Dependent {

    private SystemInterpreter systemInteractionsPlayer;

    @Override
    public Set<Command> getCommands(final CommandBuilder commandBuilder) {
        return new HashSet<Command>() {
            {
                add(commandBuilder.createDigitalCommand("system - show menu", new Command() {
                    @Override
                    public void execute(float tpf) {
                        systemInteractionsPlayer.toggleMenu();
                    }
                }));

            }
        };
    }

    @Override
    public void setTarget(AShipModelPlayer ship) {
        this.systemInteractionsPlayer = (SystemInterpreter) ship;
    }

    @Override
    public void resolveDependencies(GameModel gameModel) {
        systemInteractionsPlayer = gameModel.getSingleByType(SystemInterpreter.class);
    }

    @Override
    public void commitDependencies() {

    }
}
