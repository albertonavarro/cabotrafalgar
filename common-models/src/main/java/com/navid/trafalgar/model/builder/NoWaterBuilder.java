package com.navid.trafalgar.model.builder;

import com.navid.trafalgar.model.ModelBuilder;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.NoWater;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public final class NoWaterBuilder implements BuilderInterface {

    @Override
    public Collection build(String instanceName, Map<String, Object> customValues) {
        return Collections.singleton(new NoWater());
    }

    @Override
    public String getType() {
        return "NoWater";
    }

    @Override
    public Iterable<ModelBuilder.Category> getCategories() {
        return Collections.singleton(ModelBuilder.Category.context);
    }

}
