package com.navid.trafalgar.model.builder;

import com.jme3.math.Vector2f;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.TidalWater;
import com.navid.trafalgar.util.FormatUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public final class TidalWaterBuilder implements BuilderInterface {

    @Override
    public Collection build(String instanceName, Map<String, Object> customValues) {
        Vector2f speed = new Vector2f(0, 0);
        if (customValues.containsKey("speed")) {
            speed = FormatUtils.getVector2fFromString((String) customValues.get("speed"));
        }

        return Collections.singleton(new TidalWater(speed));
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
