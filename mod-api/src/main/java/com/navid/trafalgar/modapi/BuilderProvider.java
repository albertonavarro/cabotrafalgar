package com.navid.trafalgar.modapi;

import org.springframework.beans.factory.BeanFactory;

/**
 *
 *   
 */
public interface BuilderProvider {
    
    /**
     *
     * @param beanFactory
     */
    void registerModels(BeanFactory beanFactory); 

}
