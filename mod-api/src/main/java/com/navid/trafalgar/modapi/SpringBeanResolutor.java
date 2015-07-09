package com.navid.trafalgar.modapi;

import com.navid.nifty.flow.InstanceResolutor;
import com.navid.nifty.flow.ScreenGenerator;
import com.navid.nifty.flow.resolutors.InstanceResolutionException;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.URI;
import java.net.URISyntaxException;

public class SpringBeanResolutor implements InstanceResolutor {

    private final BeanFactory applicationContext;

    public SpringBeanResolutor(BeanFactory applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public ScreenGenerator resolveScreenGenerator(String interfaceConstructor) throws InstanceResolutionException {
        try {
            URI uri = new URI(interfaceConstructor);
            return applicationContext.getBean(uri.getSchemeSpecificPart(), ScreenGenerator.class);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Wrong format, it should be spring:beanName, found :" + interfaceConstructor, e);
        } catch (RuntimeException e) {
            throw new InstanceResolutionException("Error resolving bean " + interfaceConstructor, e);
        }
    }

    @Override
    public ScreenController resolveScreenControler(String controller) throws InstanceResolutionException {
        try {
            URI uri = new URI(controller);
            return applicationContext.getBean(uri.getSchemeSpecificPart(), ScreenController.class);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Wrong format, it should be spring:beanName, found :" + controller, e);
        } catch (RuntimeException e) {
            throw new InstanceResolutionException("Error resolving bean " + controller, e);
        }
    }

}
