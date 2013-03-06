package com.navid.trafalgar.util;

import com.jme3.math.Vector3f;
import java.text.DecimalFormat;

/**
 *
 *  
 */
public final class FormatUtils {

    /**
     * Private constructor, only static methods expected.
     */
    private FormatUtils() {
    }

    /**
     * Transforms a float into a formatted string.
     *
     * @param value
     * @return "###.###" formatted string
     */
    public static String formatFloatX_XXX(float value) {
        DecimalFormat myFormatter = new DecimalFormat("###.###");
        return myFormatter.format(value);
    }
    
    /**
     * Utility method for de-serializing vector3f from comma separated value string
     * @param input
     * @return
     */
    public static Vector3f getVector3fFromString(String input){
        String[] values = input.split(",");
        return new Vector3f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]));
    }
}
