package com.navid.trafalgar.model;

import com.navid.trafalgar.model.Builder2.Category;
import java.util.Map;

/**
 * This interface is implemented by all builders
 */
public interface BuilderInterface {

    /**
     * Actual building of the object
     * 
     * @param instanceName
     * @param customValues
     * @return
     */
    Object build( String instanceName, Map<String, String> customValues);
    
    /**
     * Returns a representative string for the object type to build, typically its class name.
     *
     * @return
     */
    String getType();
    
    /**
     * Returns a representative string for the object category to build. 
     * 
     * Not used yet, but with a palette in mind, this builder can appear under 
     * several categories in a tree structure.
     *
     * @return
     */
    Iterable<Category> getCategories();
}
