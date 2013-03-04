package com.navid.trafalgar.mod.windtunnel;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.navid.trafalgar.modapi.ModRegisterer;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author alberto
 */
public class InterfaceGenerator implements ModRegisterer {
    
    @Override
    public void generate(Nifty nifty, Screen parent, AppSettings settings, Application app, BeanFactory beanFactory) {
        XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource("mod/windtunnel/application-context.xml"), beanFactory);

        WindTunnelMainScreen mainController = ctx.getBean("mod.windtunnel.mainscreen", WindTunnelMainScreen.class);
        ScreenSelectShip shipController = (ScreenSelectShip) ctx.getBean("mod.windtunnel.shipselector");

        nifty.registerScreenController(new ScreenController[]{shipController, mainController});
        nifty.addXml("mod/windtunnel/interface_windtunnel.xml");
    }

}
