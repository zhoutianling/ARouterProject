package com.joe.common.utils;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * @author Joe
 * @time 10/15/2018 6:00 PM
 AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速

  AccelerateInterpolator  在动画开始的地方速率改变比较慢，然后开始加速

  AnticipateInterpolator 开始的时候向后然后向前甩

  AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值

  BounceInterpolator   动画结束的时候弹起

  CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线

  DecelerateInterpolator 在动画开始的地方快然后慢

  LinearInterpolator   以常量速率改变

  OvershootInterpolator    向前甩一定值后再回到原来位置
 */
public class AnimationUtils {
    /***
     * 渐进式显示与隐藏动画
     * @param view
     * @param duration
     */
    public static void showAndHiddenAnimation(View view, int duration) {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1f);
        animation.setDuration(duration);
        animation.setRepeatCount(AlphaAnimation.INFINITE);
        animation.setFillAfter(true);
        animation.setRepeatMode(AlphaAnimation.REVERSE);
        view.clearAnimation();
        view.setAnimation(animation);
        animation.start();
    }

    /***
     * 渐变放大/缩小动画
     * @param view
     * @param during
     */
    public static void showScaleAnimation(View view, int during) {
        ScaleAnimation animation = new ScaleAnimation(0, 1.0f, 0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());//在动画开始的地方速率改变比较慢，然后开始加速
        animation.setDuration(during);
        animation.setRepeatCount(AlphaAnimation.INFINITE);
        animation.setRepeatMode(AlphaAnimation.REVERSE);
        view.clearAnimation();
        view.setAnimation(animation);
        if (animation.isInitialized()) {
            animation.start();
        }
    }
}
