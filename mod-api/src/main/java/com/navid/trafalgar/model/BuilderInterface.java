package com.navid.trafalgar.model;

import com.navid.trafalgar.model.ModelBuilder.Category;
import java.util.Collection;
import java.util.Map;

public interface BuilderInterface {

    /**
     *
     * @param instanceName
     * @param customValues
     * @return
     */
    Collection buildGeometry(String instanceName, Map<String, Object> customValues);

    Collection buildControls(String instanceName, Map<String, Object> customValues);

    Collection buildCandidateRecord(String instanceName, Map<String, Object> customValues);

    Collection buildGhost(String instanceName, Map<String, Object> customValues);

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
