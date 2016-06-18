package com.navid.trafalgar.shipmodely;

import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.input.CommandBuilder;
import com.navid.trafalgar.model.AShipModelInteractive;
import com.navid.trafalgar.model.AShipModelPlayer;
import java.util.HashSet;
import java.util.Set;

public final class ShipModelTwoControlProxy implements AShipModelInteractive {

    private ShipModelTwoPlayer target;

    @Override
    public void setTarget(AShipModelPlayer target) {
        this.target = (ShipModelTwoPlayer) target;
    }

    @Override
    public Set<Command> getCommands(final CommandBuilder commandBuilder) {
        return new HashSet<Command>() {
            {
                add(commandBuilder.createCommand("tiller - to port", new Command() {
                    @Override
                    public void execute(float tpf) {
                        rudderRight(tpf*100);
                    }
                }));
                add(commandBuilder.createCommand("tiller - to starboard", new Command() {
                    @Override
                    public void execute(float tpf) {
                        rudderLeft(tpf*100);
                    }
                }));
                add(commandBuilder.createCommand("mainsail - bring in", new Command() {
                    @Override
                    public void execute(float tpf) {
                        sailTrim(tpf);
                    }
                }));
                add(commandBuilder.createCommand("mainsail - let go", new Command() {
                    @Override
                    public void execute(float tpf) {
                        sailLoose(tpf);
                    }
                }));
            }
        };
    }

    public void rudderLeft(float tpf) {
        target.rudderLeft(tpf);
    }

    public void rudderRight(float tpf) {
        target.rudderRight(tpf);
    }

    public void sailLoose(float tpf) {
        target.sailLoose(tpf);
    }

    public void sailTrim(float tpf) {
        target.sailTrim(tpf);
    }

}
