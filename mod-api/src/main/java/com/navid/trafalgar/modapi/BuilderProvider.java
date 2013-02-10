package com.navid.trafalgar.modapi;

import com.navid.trafalgar.model.Builder2;
import org.springframework.beans.factory.BeanFactory;

/**
 *
 * @author alberto
 */
public interface BuilderProvider {
    
    void registerModels(BeanFactory beanFactory); 

}
