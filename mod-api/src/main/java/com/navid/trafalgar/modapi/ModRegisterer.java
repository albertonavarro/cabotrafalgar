
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
    
    /**
     *
     * @param nifty
     * @param parent
     * @param settings
     * @param app
     * @param beanFactory
     */
    void generate(final Nifty nifty, Screen parent, AppSettings settings, Application app, BeanFactory beanFactory);
    
}
