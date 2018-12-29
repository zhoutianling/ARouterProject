package com.joe.commom_library.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Joe
 * @time 7/27/2018 11:04 AM
 */
public class MyViewPager extends ViewPager {
    public boolean isCanScroll = true;

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    //第一种
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isCanScroll) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isCanScroll) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    //第二种
//    @Override
//    public void scrollTo(int x, int y) {
//        if (isCanScroll) {
//            super.scrollTo(x, y);
//        }
//    }
}
