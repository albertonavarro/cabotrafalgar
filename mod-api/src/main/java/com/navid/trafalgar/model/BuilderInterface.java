package com.navid.trafalgar.model;

import com.navid.trafalgar.model.Builder2.Category;
import java.util.Map;

/**
 *
 *   
 */
public interface BuilderInterface {

    /**
     *
     * @param instanceName
     * @param customValues
     * @return
     */
    Object build( String instanceName, Map<String, String> customValues);
    
    /**
     *
     * @return
     */
    String getType();
    
    /**
     *
     * @return
     */
    Iterable<Category> getCategories();
}
