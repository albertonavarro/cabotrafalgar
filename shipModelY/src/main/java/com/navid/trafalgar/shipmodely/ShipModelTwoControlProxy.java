package com.navid.trafalgar.shipmodely;

import com.navid.trafalgar.input.Command;
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
    public Set<Command> getCommands() {
        return new HashSet<Command>() {
            {
                add(new Command() {
                    @Override
                    public String toString() {
                        return "rudderLeft";
                    }

                    @Override
                    public void execute(float tpf) {
                        rudderRight(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "rudderRight";
                    }

                    @Override
                    public void execute(float tpf) {
                        rudderLeft(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "sailTrim";
                    }

                    @Override
                    public void execute(float tpf) {
                        sailTrim(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "sailLoose";
                    }

                    @Override
                    public void execute(float tpf) {
                        sailLoose(tpf);
                    }
                });
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
