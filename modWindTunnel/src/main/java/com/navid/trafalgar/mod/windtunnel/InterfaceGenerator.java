package com.navid.trafalgar.mod.windtunnel;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author alberto
 */
public class InterfaceGenerator {

    private Nifty nifty;
    private Screen screen;
    private AppSettings settings;
    private Application app;

    public InterfaceGenerator(final Nifty nifty, Screen parent, AppSettings settings, Application app) {
        this.nifty = nifty;
        this.screen = parent;
        this.settings = settings;
        this.app = app;
    }

    public void generate() {
        WindTunnelMain windMain = new WindTunnelMain(settings);
        PreWindTunnelController preWind = new PreWindTunnelController( app, settings, windMain);
        nifty.registerScreenController(preWind, windMain);
        nifty.addXml("Interface/interface_windtunnel.xml");
        app.getStateManager().attach(windMain);
    }

    public void onClick() {
        nifty.gotoScreen("preWindTunnelScreen");
    }
}
