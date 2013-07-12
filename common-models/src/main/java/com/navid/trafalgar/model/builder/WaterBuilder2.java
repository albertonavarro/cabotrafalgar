package com.navid.trafalgar.model.builder;

import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.SimpleWater2;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author alberto
 */
public class WaterBuilder2 implements BuilderInterface{

    @Override
    public Collection build(String instanceName, Map<String, String> customValues) {
        return Collections.singleton(new SimpleWater2());
    }

    @Override
    public String getType() {
        return "Water2";
    }

    @Override
    public Iterable<Builder2.Category> getCategories() {
        return Collections.singleton(Builder2.Category.context);
    }
}