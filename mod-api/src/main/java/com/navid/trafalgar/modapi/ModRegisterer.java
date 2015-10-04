package com.navid.trafalgar.modapi;

import com.navid.trafalgar.audio.MusicManager;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.BeanFactory;

public interface ModRegisterer {

    void registerSpringConfig(BeanFactory beanFactory);

    void registerModels();

    void registerInputs();

    void registerScreens(Nifty nifty);

    void registerFlow(Nifty nifty);

    void registerMusic(MusicManager musicManager);

}
