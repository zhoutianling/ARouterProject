package com.joe.commom_library.widget.popup;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Joe
 * @time 7/5/2018 4:37 PM
 */
@IntDef({
        XGravity.CENTER,//水平居中
        XGravity.LEFT,//anchor view左侧
        XGravity.RIGHT,//anchor view右侧
        XGravity.ALIGN_LEFT,//与anchor view左边对齐
        XGravity.ALIGN_RIGHT,//与anchor view右边对齐
})
@Retention(RetentionPolicy.SOURCE)
public @interface XGravity {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int ALIGN_LEFT = 3;
    int ALIGN_RIGHT = 4;

}