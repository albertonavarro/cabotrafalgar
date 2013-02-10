package com.navid.trafalgar.manager.statistics;

import com.jme3.math.Vector3f;
import com.navid.trafalgar.util.FormatUtils;

/**
 *
 * @author alberto
 */
public class Vector3fStatistic extends AbstractStatistic<Vector3f>{
    
    private Vector3f value = new Vector3f(1,0,0);

    Vector3fStatistic(String parentId, String id) {
        super(parentId, id);
    }
    
    @Override
    public Vector3f getValue() {
        return value.clone();
    }

    @Override
    public void setValue(Vector3f newValue) {
        this.value = newValue.clone();
    }
    
    @Override
    public String toString(){
        return new StringBuilder()
                .append(getId())
                .append(": ")
                .append(FormatUtils.formatFloatX_XXX(value.x))
                .append(" ")
                .append(FormatUtils.formatFloatX_XXX(value.y))
                .append(" ")
                .append(FormatUtils.formatFloatX_XXX(value.z))
                .toString();
    }
    
}
