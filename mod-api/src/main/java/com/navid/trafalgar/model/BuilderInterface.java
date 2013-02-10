package com.navid.trafalgar.model;

import com.navid.trafalgar.model.Builder2.Category;
import java.util.Map;

/**
 *
 * @author alberto
 */
public interface BuilderInterface {

    Object build( String instanceName, Map<String, String> customValues);
    
    String getType();
    
    Iterable<Category> getCategories();
}
