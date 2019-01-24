package com.joe.common.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Joe
 * @time 9/30/2018 3:00 PM
 */
public class RotateUtils {
    private Map<View, ObjectAnimator> animatorMap = new HashMap<>();

    public static RotateUtils self() {
        return ClassHolder.INSTANCE;
    }

    private static class ClassHolder {
        public static final RotateUtils INSTANCE = new RotateUtils();
    }

    private void initAnimator(ImageView imageView, long duration) {
        if (null == imageView) return;
        ObjectAnimator anim = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 359f);
        animatorMap.put(imageView, anim);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
    }

    public synchronized void startRate(ImageView imageView, long duration) {
        initAnimator(imageView, duration);
        ObjectAnimator animator = animatorMap.get(imageView);
        if (null != animator && !animator.isRunning()) {
            animator.start();
        }

    }

    public synchronized void stopRotate(ImageView imageView) {
        ObjectAnimator animator = animatorMap.get(imageView);
        if (null != animator && animator.isRunning()) {
            animator.cancel();
            animatorMap.remove(animator);
        }

    }
}
