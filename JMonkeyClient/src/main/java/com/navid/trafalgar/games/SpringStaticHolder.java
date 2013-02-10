package com.navid.trafalgar.games;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author alberto
 */
public class SpringStaticHolder {

    public static XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource("application-context.xml"));
    
    public static void registerSingletonBeanDefinition( String name, String className ){
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinition.setBeanClassName(className);
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        ctx.registerBeanDefinition(name, beanDefinition);
        
    }
    
    public static void registerPrototypeBeanDefinition( String name, String className){
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        beanDefinition.setBeanClassName(className);
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        ctx.registerBeanDefinition(name, beanDefinition);
    }
    
    public static void registerBean(String name, Object object){
            ctx.registerSingleton(name, object);
    }
}
