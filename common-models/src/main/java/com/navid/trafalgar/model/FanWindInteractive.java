package com.navid.trafalgar.model;

import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.input.CommandBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alberto on 28/12/16.
 */
public class FanWindInteractive implements AShipModelInteractive, Dependent {

    private FanWind iWind;

    public FanWindInteractive(IWind iWind) {

    }

    @Override
    public Set<Command> getCommands(final CommandBuilder commandBuilder) {
        return new HashSet<Command>() {
            {
                add(commandBuilder.createCommand("fan - rotate clockwise", new Command() {
                    @Override
                    public void execute(float tpf) {
                        iWind.rotate(0,1,0);
                    }
                }));
                add(commandBuilder.createCommand("fan - rotate counterclockwise", new Command() {
                    @Override
                    public void execute(float tpf) {
                        iWind.rotate(0,-1,0);
                    }
                }));
            }
        };
    }

    @Override
    public void setTarget(AShipModelPlayer ship) {

    }

    @Override
    public void resolveDependencies(GameModel gameModel) {
        iWind = gameModel.getSingleByType(FanWind.class);
    }

    @Override
    public void commitDependencies() {

    }
}
