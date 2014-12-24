package com.navid.trafalgar.modapi;

import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.BeanFactory;

public interface ModRegisterer {

    void registerSpringConfig(BeanFactory beanFactory);

    void registerModels();

    void registerInputs();

    void registerScreens(Nifty nifty);

    void registerFlow(Nifty nifty);

}
