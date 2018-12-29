package com.joe.commom_library.utils;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * @author Joe
 * @time 10/18/2018 2:00 PM
 */
public class VibratorUtils {

    public static void vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
}
