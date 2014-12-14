package com.navid.trafalgar.model.builder;

import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.NoWater;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class NoWaterBuilder implements BuilderInterface {

    @Override
    public Collection build(String instanceName, Map<String, Object> customValues) {
        return Collections.singleton(new NoWater());
    }

    @Override
    public String getType() {
        return "NoWater";
    }

    @Override
    public Iterable<Builder2.Category> getCategories() {
        return Collections.singleton(Builder2.Category.context);
    }

}
