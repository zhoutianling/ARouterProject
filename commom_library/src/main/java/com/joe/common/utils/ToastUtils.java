package com.joe.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joe.common.R;

/**
 * @author Joe
 * @time 10/15/2018 11:39 AM
 */
public class ToastUtils {
    private static Context sContext;
    private static int TEXT_SIZE = 15;

    private ToastUtils() {

    }

    private static Context getsContext() {
        if (sContext != null) {
            return sContext;
        } else {
            throw new NullPointerException("u should call T.init() first");
        }
    }

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static void init(Context context, int textSize) {
        sContext = context.getApplicationContext();
        TEXT_SIZE = textSize;
    }

    /**
     * infomation toast  color: primary
     *
     * @param msg message
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Toast i(String msg) {
        Toast custom = create(msg);
        View view = custom.getView();
        Drawable drawable =
                getsContext().getResources().getDrawable(R.drawable.shape_round_corner_custom_toast);
        GradientDrawable gradientDrawable = (GradientDrawable) drawable;
        gradientDrawable.setColor(Color.parseColor("#FF7200"));
        view.setBackground(gradientDrawable);
        ImageView imageView = view.findViewById(R.id.imageview_custom_handsome_toast);
        imageView.setImageResource(R.mipmap.ic_info);
        custom.show();
        return custom;
    }

    /**
     * e toast  color:red
     *
     * @param msg message
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Toast e(String msg) {
        Toast custom = create(msg);
        View view = custom.getView();
        Drawable drawable =
                getsContext().getResources().getDrawable(R.drawable.shape_round_corner_custom_toast);
        GradientDrawable gradientDrawable = (GradientDrawable) drawable;
        gradientDrawable.setColor(Color.parseColor("#D50000"));
        view.setBackground(gradientDrawable);
        ImageView imageView = view.findViewById(R.id.imageview_custom_handsome_toast);
        imageView.setImageResource(R.mipmap.ic_error);
        custom.show();
        return custom;
    }

    /**
     * s toast color:green
     *
     * @param msg message
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Toast s(String msg) {
        Toast custom = create(msg);
        View view = custom.getView();
        Drawable drawable =
                getsContext().getResources().getDrawable(R.drawable.shape_round_corner_custom_toast);
        GradientDrawable gradientDrawable = (GradientDrawable) drawable;
        gradientDrawable.setColor(Color.parseColor("#388E3C"));
        view.setBackground(gradientDrawable);
        ImageView imageView = view.findViewById(R.id.imageview_custom_handsome_toast);
        imageView.setImageResource(R.mipmap.ic_success);
        custom.show();
        return custom;
    }

    /**
     * custom icon 、 bgcolor
     *
     * @param iconRes icon
     * @param bgColor toast bg color
     * @param msg     message
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Toast custom(@DrawableRes int iconRes, @ColorInt int bgColor, String msg) {
        Toast custom = create(msg);
        View view = custom.getView();
        Drawable drawable =
                getsContext().getResources().getDrawable(R.drawable.shape_round_corner_custom_toast);
        GradientDrawable gradientDrawable = (GradientDrawable) drawable;
        gradientDrawable.setColor(bgColor);
        view.setBackground(gradientDrawable);
        ImageView imageView = view.findViewById(R.id.imageview_custom_handsome_toast);
        imageView.setImageResource(iconRes);
        custom.show();
        return custom;
    }

    /**
     * create a create toast basic method
     *
     * @param msg message
     * @return Toast   toast
     */
    private static Toast create(String msg) {
        Toast toast = Toast.makeText(getsContext(), "", Toast.LENGTH_SHORT);
        toast.setText(msg);

        LinearLayout rootView = (LinearLayout) toast.getView();
        rootView.setOrientation(LinearLayout.HORIZONTAL);
        // imageView  i partten default
        ImageView imageView = new ImageView(getsContext());
        imageView.setId(R.id.imageview_custom_handsome_toast);
        imageView.setPadding(60, 0, 20, 0);
        imageView.setImageResource(R.mipmap.ic_info);
        LinearLayout.LayoutParams imagviewParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        imagviewParams.gravity = Gravity.CENTER_VERTICAL;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(imagviewParams);
        rootView.addView(imageView, 0);

        TextView toastText = (TextView) rootView.findViewById(android.R.id.message);
        toastText.setTextSize(TEXT_SIZE);
        toastText.setTextColor(Color.WHITE);
        toastText.setPadding(0, 0, 60, 0);
        LinearLayout.LayoutParams toastTextParams =
                (LinearLayout.LayoutParams) toastText.getLayoutParams();
        toastTextParams.gravity = Gravity.CENTER_VERTICAL;
        toastText.setLayoutParams(toastTextParams);

        rootView.setBackgroundResource(R.drawable.shape_round_corner_custom_toast);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }
}
