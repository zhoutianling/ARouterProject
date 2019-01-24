package com.joe.common.utils;

import android.graphics.Color;

/**
 * @author Joe
 * @time 7/26/2018 5:34 PM
 */
public class ColorUtils {
    /***
     * 十进制转十六进制
     * @param color
     * @return
     */
    public static String getRgb(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        String rgb = String.format("#%02x%02x%02x", red, green, blue);
        return rgb;
    }

    public static Integer[] getColor(String rgb) {
        int color = Color.parseColor(rgb);
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        return new Integer[]{red, green, blue};
    }
}
