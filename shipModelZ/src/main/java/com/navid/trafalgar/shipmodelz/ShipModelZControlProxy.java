package com.navid.trafalgar.shipmodelz;

import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.input.CommandBuilder;
import com.navid.trafalgar.model.AShipModelInteractive;
import com.navid.trafalgar.model.AShipModelPlayer;
import com.navid.trafalgar.model.Dependent;
import com.navid.trafalgar.model.GameModel;

import java.util.HashSet;
import java.util.Set;

public final class ShipModelZControlProxy implements AShipModelInteractive, Dependent {

    private ShipModelZPlayer target;

    @Override
    public void setTarget(AShipModelPlayer target) {
        this.target = (ShipModelZPlayer) target;
    }

    @Override
    public Set<Command> getCommands(final CommandBuilder commandBuilder) {
        return new HashSet<Command>() {
            {
                add(commandBuilder.createCommand("tiller - to port", new Command() {
                    @Override
                    public void execute(float tpf) {
                        target.rudderRight(tpf);
                    }
                }));
                add(commandBuilder.createCommand("tiller - to starboard", new Command() {
                    @Override
                    public void execute(float tpf) {
                        target.rudderLeft(tpf);
                    }
                }));
                add(commandBuilder.createCommand("mainsail - bring in", new Command() {
                    @Override
                    public void execute(float tpf) {
                        target.sailTrim(tpf);
                    }
                }));
                add(commandBuilder.createCommand("mainsail - let go", new Command() {
                    @Override
                    public void execute(float tpf) {
                        target.sailLoose(tpf);
                    }
                }));
                add(commandBuilder.createCommand("weight - move to port", new Command() {
                    @Override
                    public void execute(float tpf) {
                        target.weightPort(tpf);
                    }
                }));
                add(commandBuilder.createCommand("weight - move to starboard", new Command() {
                    @Override
                    public void execute(float tpf) {
                        target.weightStarboard(tpf);
                    }
                }));
            }
        };
    }

    @Override
    public void resolveDependencies(GameModel gameModel) {
        target = gameModel.getSingleByType(ShipModelZPlayer.class);
    }

    @Override
    public void commitDependencies() {

    }
}
