package com.navid.trafalgar.modapi;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import org.springframework.beans.factory.BeanFactory;

/**
 *
 *
 */
public interface ModRegisterer {

    void registerSpringConfig(BeanFactory beanFactory);

    void registerModels();

    void registerInputs();

    void registerScreens(Nifty nifty);

    void registerFlow(Nifty nifty);

}
