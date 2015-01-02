package com.navid.trafalgar.model.builder;

import com.navid.trafalgar.model.ModelBuilder;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.SimpleWater;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public final class WaterBuilder implements BuilderInterface {

    @Override
    public Collection build(String instanceName, Map<String, Object> customValues) {
        return Collections.singleton(new SimpleWater());
    }

    @Override
    public String getType() {
        return "SimpleWater";
    }

    @Override
    public Iterable<ModelBuilder.Category> getCategories() {
        return Collections.singleton(ModelBuilder.Category.context);
    }
}
