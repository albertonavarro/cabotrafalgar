package com.navid.trafalgar.modapi;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.navid.trafalgar.model.Builder2;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author casa
 */
public class ModRegistererClasspathDetector implements ApplicationContextAware {
    
    private static final Logger LOG = LoggerFactory.getLogger(ModRegistererClasspathDetector.class);
    
    private ApplicationContext ctx;
    
    private Builder2 builder;
    
    public void detectAndRegister() {
        loadModules(null, null, null, null);
    }
    
    private void loadModules(Nifty nifty, Screen screen, AppSettings settings, Application app) {
        Reflections reflections = new Reflections("com.navid.trafalgar");

        Set<Class<? extends ModRegisterer>> modules = reflections.getSubTypesOf(ModRegisterer.class);

        Collection<ModRegisterer> resultInstances = new ArrayList();

        for (Class<? extends ModRegisterer> currentClass : modules) {
            try {
                ModRegisterer currentLoader = (ModRegisterer) Class.forName(currentClass.getCanonicalName()).newInstance();
                resultInstances.add(currentLoader);
            } catch (ClassNotFoundException ex) {
                LOG.error("Error loading module {}", currentClass, ex);
            } catch (InstantiationException ex) {
                LOG.error("Error loading module {}", currentClass, ex);
            } catch (IllegalAccessException ex) {
                LOG.error("Error loading module {}", currentClass, ex);
            }
        }

        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerSpringConfig(ctx);
        }
        
        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerInputs();
        }
        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerModels();
        }
        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerScreens(nifty);
        }
        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerFlow(nifty);
        }

    }

    /**
     * @param builder the builder to set
     */
    public void setBuilder(Builder2 builder) {
        this.builder = builder;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
    
    
    
}
