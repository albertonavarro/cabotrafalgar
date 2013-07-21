package com.navid.trafalgar.mod.windtunnel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.navid.trafalgar.modapi.GenericModRegisterer;
import com.navid.trafalgar.modapi.ModConfiguration;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.BeanFactory;

/**
 *
 * @author alberto
 */
public class InterfaceGeneratorGeneric extends GenericModRegisterer {
    
    @Override
    public void generate(Nifty nifty, Screen parent, AppSettings settings, Application app, BeanFactory beanFactory) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            ModConfiguration config = mapper.readValue(this.getClass().getResourceAsStream("windtunnelmodconfig.yml"), ModConfiguration.class);
            super.generate(nifty, parent, settings, app, beanFactory, config);
        } catch (IOException ex) {
            Logger.getLogger(InterfaceGeneratorGeneric.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
