package com.navid.trafalgar.mod.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.navid.trafalgar.modapi.GenericModRegisterer;
import com.navid.trafalgar.modapi.ModConfiguration;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.BeanFactory;

/**
 *
 * @author alberto
 */
public class InterfaceGenerator extends GenericModRegisterer{

    @Override
    public void generate(Nifty nifty, Screen parent, AppSettings settings, Application app, BeanFactory beanFactory) {
        
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            
            ModConfiguration config = mapper.readValue(new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("com.navid.trafalgar.mod.common/commonmodconfig.yml"))), ModConfiguration.class);
            
            //ModConfiguration config = mapper.readValue(this.getClass().getResourceAsStream("mod/common/builders-declaration.xml"), ModConfiguration.class);
            super.generate(nifty, parent, settings, app, beanFactory, config);
        } catch (IOException ex) {
            Logger.getLogger(InterfaceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
