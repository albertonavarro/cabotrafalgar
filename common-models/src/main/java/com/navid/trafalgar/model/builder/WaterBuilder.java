package com.navid.trafalgar.model.builder;

import com.navid.trafalgar.model.ModelBuilder;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.SimpleWater;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

public final class WaterBuilder implements BuilderInterface {

    @Override
    public Collection buildGeometry(String instanceName, Map<String, Object> customValues) {
        return singleton(new SimpleWater());
    }

    @Override
    public Collection buildControls(String instanceName, Map<String, Object> customValues) {
        return emptyList();
    }

    @Override
    public Collection buildCandidateRecord(String instanceName, Map<String, Object> customValues) {
        return emptyList();
    }

    @Override
    public Collection buildGhost(String instanceName, Map<String, Object> customValues) {
        return emptyList();
    }

    @Override
    public String getType() {
        return "SimpleWater";
    }

    @Override
    public Iterable<ModelBuilder.Category> getCategories() {
        return singleton(ModelBuilder.Category.context);
    }
}
