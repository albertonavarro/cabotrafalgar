package com.navid.trafalgar.manager.statistics;

import com.navid.trafalgar.util.FormatUtils;

/**
 * Float implementation for AbstractStatistic
 */
public final class FloatStatistic extends AbstractStatistic<Float> {

    private Float value = null;

    FloatStatistic(String parentId, String id, float initValue) {
        super(parentId, id);
        this.value = initValue;
    }

    /**
     *
     * @return
     */
    @Override
    public Float getValue() {
        if (value == null) {
            return 0f;
        }

        return value;
    }

    /**
     *
     * @param newValue
     */
    @Override
    public void setValue(Float newValue) {
        this.value = newValue;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return getId() + " " + FormatUtils.formatFloatX_XXX(getValue());
    }

}
