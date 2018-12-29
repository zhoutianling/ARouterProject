package com.joe.commom_library.widget.popup;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Joe
 * @time 7/5/2018 4:37 PM
 */

@IntDef({
        YGravity.CENTER,//垂直居中
        YGravity.ABOVE,//anchor view之上
        YGravity.BELOW,//anchor view之下
        YGravity.ALIGN_TOP,//与anchor view顶部对齐
        YGravity.ALIGN_BOTTOM,//anchor view底部对齐
})
@Retention(RetentionPolicy.SOURCE)
public @interface YGravity {
    int CENTER = 0;
    int ABOVE = 1;
    int BELOW = 2;
    int ALIGN_TOP = 3;
    int ALIGN_BOTTOM = 4;
}
