package com.navid.trafalgar.util;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.text.DecimalFormat;

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
    public static String formatFloat(float value) {
        DecimalFormat myFormatter = new DecimalFormat("###.###");
        return myFormatter.format(value);
    }

    /**
     *
     * @param input
     * @return
     */
    public static Vector3f getVector3fFromString(String input) {
        String[] values = input.split(",");
        return new Vector3f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]));
    }

    /**
     *
     * @param input
     * @return
     */
    public static Vector2f getVector2fFromString(String input) {
        String[] values = input.split(",");
        return new Vector2f(Float.parseFloat(values[0]), Float.parseFloat(values[1]));
    }
}
