package com.navid.trafalgar.model.builder;

import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.TidalWater;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public final class TidalWaterBuilder implements BuilderInterface {

    @Override
    public Collection build(String instanceName, Map<String, Object> customValues) {
        return Collections.singleton(new TidalWater());
    }

    @Override
    public String getType() {
        return "TidalWater";
    }

    @Override
    public Iterable<Builder2.Category> getCategories() {
        return Collections.singleton(Builder2.Category.context);
    }
    
}
