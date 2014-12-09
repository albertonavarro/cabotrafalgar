/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.model;

import com.navid.trafalgar.input.Command;
import java.util.Set;

/**
 *
 * @author casa
 */
public interface AShipModelInteractive {

    Set<Command> getCommands();

    void setTarget(AShipModelPlayer ship);

}
