package com.navid.trafalgar.input;

import com.navid.trafalgar.model.AShipModelPlayer;

/**
 * Created by alberto on 08/05/16.
 */
public interface SystemInterpreter extends AShipModelPlayer {
    void showMenu();

    void showControls();
}