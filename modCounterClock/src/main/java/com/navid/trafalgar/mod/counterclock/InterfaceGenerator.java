package com.navid.trafalgar.mod.counterclock;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.navid.trafalgar.modapi.BuilderProvider;
import com.navid.trafalgar.modapi.ModRegisterer;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.Map;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author alberto
 */
public class InterfaceGenerator implements ModRegisterer, BuilderProvider {

    @Override
    public void generate(final Nifty nifty, Screen parent, AppSettings settings, Application app, BeanFactory beanFactory) {
        XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource("mod/counterclock/application-context.xml"), beanFactory);

        CounterClockMainScreen mainController = ctx.getBean("mod.counterclock.mainscreen", CounterClockMainScreen.class);
        ScreenSelectMap mapController = (ScreenSelectMap) ctx.getBean("mod.counterclock.mapselector");
        ScreenSelectShip shipController = (ScreenSelectShip) ctx.getBean("mod.counterclock.shipselector");

        nifty.registerScreenController(new ScreenController[]{mapController, shipController, mainController});
        nifty.addXml("mod/counterclock/interface_counterclock.xml");
    }

    @Override
    public void registerModels(BeanFactory beanFactory) {
        XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource("mod/counterclock/builders-declaration.xml"), beanFactory);

        Map<String, BuilderInterface> builders = ctx.getBeansOfType(BuilderInterface.class);
        Builder2 builder2 = (Builder2) beanFactory.getBean("common.Builder");

        for (BuilderInterface currentBuilder : builders.values()) {
            builder2.registerBuilder(currentBuilder);
        }
    }
}
