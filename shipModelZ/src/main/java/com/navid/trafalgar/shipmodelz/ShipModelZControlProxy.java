package com.navid.trafalgar.shipmodelz;

import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.model.AShipModelInteractive;
import com.navid.trafalgar.model.AShipModelPlayer;
import java.util.HashSet;
import java.util.Set;

public class ShipModelZControlProxy implements AShipModelInteractive {

    private ShipModelZPlayer target;

    @Override
    public void setTarget(AShipModelPlayer target) {
        this.target = (ShipModelZPlayer) target;
    }

    @Override
    public Set<Command> getCommands() {
        return new HashSet<Command>() {
            {
                add(new Command() {
                    @Override
                    public String toString() {
                        return "tiller - to port";
                    }

                    @Override
                    public void execute(float tpf) {
                        target.rudderRight(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "tiller - to starboard";
                    }

                    @Override
                    public void execute(float tpf) {
                        target.rudderLeft(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "mainsail - bring in";
                    }

                    @Override
                    public void execute(float tpf) {
                        target.sailTrim(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "mainsail - let go";
                    }

                    @Override
                    public void execute(float tpf) {
                        target.sailLoose(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "weight- move to port";
                    }

                    @Override
                    public void execute(float tpf) {
                        target.weightPort(tpf);
                    }
                });
                add(new Command() {
                    @Override
                    public String toString() {
                        return "weight - move to starboard";
                    }

                    @Override
                    public void execute(float tpf) {
                        target.weightStarboard(tpf);
                    }
                });
            }
        };
    }

}
