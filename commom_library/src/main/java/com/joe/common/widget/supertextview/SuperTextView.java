

/*
 * Copyright (C) 2017 CoorChice <icechen_@outlook.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * <p>
 * Last modified 17-4-20 下午5:32
 */

package com.joe.common.widget.supertextview;


import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.joe.common.R;
import com.joe.common.widget.supertextview.sys_adjusters.PressAdjuster;
import com.joe.common.widget.supertextview.utils.STVUtils;

import java.util.ArrayList;
import java.util.List;


public class SuperTextView extends android.support.v7.widget.AppCompatTextView {

    public static final int NO_COLOR = -99;
    // Some Property Default Value
    private static final float DEFAULT_CORNER = 0f;
    private static final int DEFAULT_SOLID = Color.TRANSPARENT;
    private static final float DEFAULT_STROKE_WIDTH = 0f;
    private static final int DEFAULT_STROKE_COLOR = Color.BLACK;
    private static final int DEFAULT_STATE_DRAWABLE_MODE = DrawableMode.CENTER.code;
    private static final int DEFAULT_TEXT_STROKE_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_FILL_COLOR = Color.BLACK;
    private static final float DEFAULT_TEXT_STROKE_WIDTH = 0f;
    private static final int ALLOW_CUSTOM_ADJUSTER_SIZE = 3;
    private int SYSTEM_ADJUSTER_SIZE = 0;

    private float corner;
    private boolean leftTopCornerEnable;
    private boolean rightTopCornerEnable;
    private boolean leftBottomCornerEnable;
    private boolean rightBottomCornerEnable;
    private int solid;
    private float strokeWidth;
    private int strokeColor;
    private DrawableMode stateDrawableMode;
    private DrawableMode stateDrawable2Mode;
    private boolean isShowState;
    private boolean isShowState2;

    private Paint paint;
    private int width;
    private int height;
    private Drawable drawable;
    private Drawable drawable2;
    private float density;
    private boolean autoAdjust;
    // private Adjuster adjuster;
    private Adjuster pressAdjuster;
    private boolean textStroke;
    private int textStrokeColor;
    private int textFillColor;
    private float textStrokeWidth;
    private boolean runnable = false;
    private boolean needRun = false;
    private Thread animThread;
    private Path strokeWidthPath;
    private Path solidPath;
    private RectF strokeLineRectF;
    private RectF solidRectF;
    private float leftTopCorner[] = new float[2];
    private float rightTopCorner[] = new float[2];
    private float leftBottomCorner[] = new float[2];
    private float rightBottomCorner[] = new float[2];
    private float corners[] = new float[8];
    private float[] drawableBounds = new float[4];
    private float drawableWidth;
    private float drawableHeight;
    private float drawablePaddingLeft;
    private float drawablePaddingTop;
    private float[] drawable2Bounds = new float[4];
    private float drawable2Width;
    private float drawable2Height;
    private float drawable2PaddingLeft;
    private float drawable2PaddingTop;
    private boolean cacheRunnableState;
    private boolean cacheNeedRunState;
    private int frameRate = 60;
    private Runnable invalidate;
    private int shaderStartColor;
    private int shaderEndColor;
    private ShaderMode shaderMode;
    private LinearGradient shader;
    private boolean shaderEnable;
    private int pressBgColor;
    private int pressTextColor;
    private boolean drawableAsBackground;
    private BitmapShader drawableBackgroundShader;

    private List<Adjuster> adjusterList = new ArrayList<>();
    private List<Adjuster> touchAdjusters = new ArrayList<>();
    private Runnable handleAnim;
    private boolean superTouchEvent;
    private String curImageUrl;


    /**
     * 简单的构造函数
     *
     * @param context View运行的Context环境
     */
    public SuperTextView(Context context) {
        super(context);
        init(null);
    }

    /**
     * inflate Xml布局文件时会被调用
     *
     * @param context View运行的Context环境
     * @param attrs   View在xml布局文件中配置的属性集合对象
     */
    public SuperTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * 略
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SuperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        density = getContext().getResources().getDisplayMetrics().density;
        initAttrs(attrs);
        paint = new Paint();
        initPaint();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray =
                    getContext().obtainStyledAttributes(attrs, R.styleable.SuperTextView);
            corner = typedArray.getDimension(R.styleable.SuperTextView_corner, DEFAULT_CORNER);
            leftTopCornerEnable =
                    typedArray.getBoolean(R.styleable.SuperTextView_left_top_corner, false);
            rightTopCornerEnable =
                    typedArray.getBoolean(R.styleable.SuperTextView_right_top_corner, false);
            leftBottomCornerEnable =
                    typedArray.getBoolean(R.styleable.SuperTextView_left_bottom_corner, false);
            rightBottomCornerEnable =
                    typedArray.getBoolean(R.styleable.SuperTextView_right_bottom_corner, false);
            solid = typedArray.getColor(R.styleable.SuperTextView_solid, DEFAULT_SOLID);
            strokeWidth = typedArray.getDimension(R.styleable.SuperTextView_stroke_width,
                    DEFAULT_STROKE_WIDTH);
            strokeColor =
                    typedArray.getColor(R.styleable.SuperTextView_stroke_color, DEFAULT_STROKE_COLOR);
            drawable = typedArray.getDrawable(R.styleable.SuperTextView_state_drawable);
            drawableWidth =
                    typedArray.getDimension(R.styleable.SuperTextView_state_drawable_width, 0);
            drawableHeight =
                    typedArray.getDimension(R.styleable.SuperTextView_state_drawable_height, 0);
            drawablePaddingLeft =
                    typedArray.getDimension(R.styleable.SuperTextView_state_drawable_padding_left, 0);
            drawablePaddingTop =
                    typedArray.getDimension(R.styleable.SuperTextView_state_drawable_padding_top, 0);
            drawable2 = typedArray.getDrawable(R.styleable.SuperTextView_state_drawable2);
            drawable2Width =
                    typedArray.getDimension(R.styleable.SuperTextView_state_drawable2_width, 0);
            drawable2Height =
                    typedArray.getDimension(R.styleable.SuperTextView_state_drawable2_height, 0);
            drawable2PaddingLeft =
                    typedArray.getDimension(R.styleable.SuperTextView_state_drawable2_padding_left, 0);
            drawable2PaddingTop =
                    typedArray.getDimension(R.styleable.SuperTextView_state_drawable2_padding_top, 0);
            isShowState = typedArray.getBoolean(R.styleable.SuperTextView_isShowState, false);
            drawableAsBackground =
                    typedArray.getBoolean(R.styleable.SuperTextView_drawableAsBackground, false);
            isShowState2 = typedArray.getBoolean(R.styleable.SuperTextView_isShowState2, false);
            stateDrawableMode = DrawableMode.valueOf(typedArray.getInteger(R.styleable.SuperTextView_state_drawable_mode,
                    DEFAULT_STATE_DRAWABLE_MODE));
            stateDrawable2Mode = DrawableMode.valueOf(typedArray.getInteger(R.styleable.SuperTextView_state_drawable2_mode,
                    DEFAULT_STATE_DRAWABLE_MODE));
            textStroke = typedArray.getBoolean(R.styleable.SuperTextView_text_stroke, false);
            textStrokeColor = typedArray.getColor(R.styleable.SuperTextView_text_stroke_color,
                    DEFAULT_TEXT_STROKE_COLOR);
            textFillColor = typedArray.getColor(R.styleable.SuperTextView_text_fill_color,
                    DEFAULT_TEXT_FILL_COLOR);
            textStrokeWidth = typedArray.getDimension(R.styleable.SuperTextView_text_stroke_width,
                    DEFAULT_TEXT_STROKE_WIDTH);
            autoAdjust = typedArray.getBoolean(R.styleable.SuperTextView_autoAdjust, false);
            shaderStartColor =
                    typedArray.getColor(R.styleable.SuperTextView_shaderStartColor, 0);
            shaderEndColor =
                    typedArray.getColor(R.styleable.SuperTextView_shaderEndColor, 0);
            shaderMode = ShaderMode.valueOf(typedArray.getInteger(R.styleable.SuperTextView_shaderMode, ShaderMode.TOP_TO_BOTTOM.code));
            shaderEnable = typedArray.getBoolean(R.styleable.SuperTextView_shaderEnable, false);
            pressBgColor = typedArray.getColor(R.styleable.SuperTextView_pressBgColor, Color.TRANSPARENT);
            pressTextColor = typedArray.getColor(R.styleable.SuperTextView_pressTextColor, -99);
            typedArray.recycle();
        }
    }

    private void initPaint() {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw && h != oldh) {
            drawableBackgroundShader = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        drawStrokeLine(canvas);
        drawSolid(canvas);
        checkPressColor(canvas);
        isNeedToAdjust(canvas, Adjuster.Opportunity.BEFORE_DRAWABLE);
        drawStateDrawable(canvas);
        isNeedToAdjust(canvas, Adjuster.Opportunity.BEFORE_TEXT);
        if (textStroke) {
            getPaint().setStyle(Paint.Style.STROKE);
            setTextColor(textStrokeColor);
            getPaint().setFakeBoldText(true);
            getPaint().setStrokeWidth(textStrokeWidth);
            super.onDraw(canvas);
            getPaint().setStyle(Paint.Style.FILL);
            getPaint().setFakeBoldText(false);
            setTextColor(textFillColor);
        }
        super.onDraw(canvas);
        isNeedToAdjust(canvas, Adjuster.Opportunity.AT_LAST);
    }

    private void drawStrokeLine(Canvas canvas) {
        if (strokeWidth > 0) {
            if (strokeWidthPath == null) {
                strokeWidthPath = new Path();
            } else {
                strokeWidthPath.reset();
            }
            if (strokeLineRectF == null) {
                strokeLineRectF = new RectF();
            } else {
                strokeLineRectF.setEmpty();
            }
            strokeLineRectF.set(strokeWidth / 2, strokeWidth / 2, width - strokeWidth / 2,
                    height - strokeWidth / 2);
            getCorners(corner);
            strokeWidthPath.addRoundRect(strokeLineRectF, corners, Path.Direction.CW);
            initPaint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(strokeColor);
            paint.setStrokeWidth(strokeWidth);
            canvas.drawPath(strokeWidthPath, paint);
        }
    }

    private void drawSolid(Canvas canvas) {
        if (solidPath == null) {
            solidPath = new Path();
        } else {
            solidPath.reset();
        }
        if (solidRectF == null) {
            solidRectF = new RectF();
        } else {
            solidRectF.setEmpty();
        }
        solidRectF.set(strokeWidth, strokeWidth, width - strokeWidth, height - strokeWidth);
        getCorners(corner - strokeWidth / 2);
        solidPath.addRoundRect(solidRectF, corners, Path.Direction.CW);

        initPaint();
        paint.setStyle(Paint.Style.FILL);
        if (shaderEnable) {
            useShader(paint);
        } else {
            paint.setColor(solid);
        }
        canvas.drawPath(solidPath, paint);
    }

    private float[] getCorners(float corner) {
        leftTopCorner[0] = 0;
        leftTopCorner[1] = 0;
        rightTopCorner[0] = 0;
        rightTopCorner[1] = 0;
        leftBottomCorner[0] = 0;
        leftBottomCorner[1] = 0;
        rightBottomCorner[0] = 0;
        rightBottomCorner[1] = 0;
        if (this.leftTopCornerEnable || this.rightTopCornerEnable || this.leftBottomCornerEnable
                || this.rightBottomCornerEnable) {
            if (this.leftTopCornerEnable) {
                leftTopCorner[0] = corner;
                leftTopCorner[1] = corner;
            }
            if (this.rightTopCornerEnable) {
                rightTopCorner[0] = corner;
                rightTopCorner[1] = corner;
            }
            if (this.leftBottomCornerEnable) {
                leftBottomCorner[0] = corner;
                leftBottomCorner[1] = corner;
            }
            if (this.rightBottomCornerEnable) {
                rightBottomCorner[0] = corner;
                rightBottomCorner[1] = corner;
            }
        } else {
            leftTopCorner[0] = corner;
            leftTopCorner[1] = corner;
            rightTopCorner[0] = corner;
            rightTopCorner[1] = corner;
            leftBottomCorner[0] = corner;
            leftBottomCorner[1] = corner;
            rightBottomCorner[0] = corner;
            rightBottomCorner[1] = corner;
        }
        corners[0] = leftTopCorner[0];
        corners[1] = leftTopCorner[1];
        corners[2] = rightTopCorner[0];
        corners[3] = rightTopCorner[1];
        corners[4] = rightBottomCorner[0];
        corners[5] = rightBottomCorner[1];
        corners[6] = leftBottomCorner[0];
        corners[7] = leftBottomCorner[1];
        return corners;
    }

    /**
     * 获取SuperTextView的详细圆角信息，共4个圆角，每个圆角包含两个值。
     *
     * @return 返回SuperTextView的圆角信息 {@link SuperTextView#getCorners(float)}.
     */
    public float[] getCorners() {
        return corners;
    }

    private void checkPressColor(Canvas canvas) {
        if (pressBgColor != Color.TRANSPARENT || pressTextColor != -99) {
            if (pressAdjuster == null) {
                pressAdjuster = new PressAdjuster(pressBgColor)
                        .setPressTextColor(pressTextColor);
                addSysAdjuster(pressAdjuster);
            }
            ((PressAdjuster) pressAdjuster).setPressTextColor(pressTextColor);
            ((PressAdjuster) pressAdjuster).setPressBgColor(pressBgColor);
        }
    }

    private void useShader(Paint paint) {
        if (shader == null) {
            createShader();
        }
        paint.setShader(shader);
    }

    private boolean createShader() {
        if (shaderStartColor != 0 && shaderEndColor != 0) {
            float x0 = 0;
            float x1 = 0;
            float y0 = 0;
            float y1 = 0;
            int startColor = shaderStartColor;
            int endColor = shaderEndColor;
            switch (shaderMode) {
                case TOP_TO_BOTTOM:
                    y1 = height;
                    break;
                case BOTTOM_TO_TOP:
                    y1 = height;
                    startColor = shaderEndColor;
                    endColor = shaderStartColor;
                    break;
                case LEFT_TO_RIGHT:
                    x1 = width;
                    break;
                case RIGHT_TO_LEFT:
                    x1 = width;
                    startColor = shaderEndColor;
                    endColor = shaderStartColor;
                    break;
            }
            shader = new LinearGradient(x0, y0, x1, y1, startColor, endColor, Shader.TileMode.CLAMP);
            return true;
        } else {
            return false;
        }
    }

    private void drawStateDrawable(Canvas canvas) {
        if (drawable != null) {
            if (drawableAsBackground) {
                drawDrawableBackground(canvas);
            } else if (isShowState) {
                getDrawableBounds();
                drawable.setBounds((int) drawableBounds[0], (int) drawableBounds[1],
                        (int) drawableBounds[2], (int) drawableBounds[3]);
                drawable.draw(canvas);
            }
        }

        if (drawable2 != null && isShowState2) {
            getDrawable2Bounds();
            drawable2.setBounds((int) drawable2Bounds[0], (int) drawable2Bounds[1],
                    (int) drawable2Bounds[2], (int) drawable2Bounds[3]);
            drawable2.draw(canvas);
        }
    }

    private void drawDrawableBackground(Canvas canvas) {
        if (drawableBackgroundShader == null) {
            Bitmap bitmap = STVUtils.drawableToBitmap(drawable);
            bitmap = computeSuitedBitmapSize(bitmap);
            drawableBackgroundShader =
                    new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }

        Shader shader = paint.getShader();
        int color = paint.getColor();
        paint.setColor(Color.WHITE);
        paint.setShader(drawableBackgroundShader);
        canvas.drawPath(solidPath, paint);
        paint.setShader(shader);
        paint.setColor(color);
    }

    private Bitmap computeSuitedBitmapSize(Bitmap bitmap) {
        int suitedWidth = width;
        int suitedHeight = height;
        if ((float) bitmap.getWidth() / (float) width > (float) bitmap.getHeight()
                / (float) height) {
            suitedWidth = (int) (((float) bitmap.getWidth() / (float) bitmap.getHeight())
                    * (float) suitedHeight);
        } else {
            suitedHeight = (int) ((float) suitedWidth
                    / ((float) bitmap.getWidth() / (float) bitmap.getHeight()));
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, suitedWidth, suitedHeight, true);
        bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2 - width / 2,
                bitmap.getHeight() / 2 - height / 2, width, height);
        return bitmap;
    }

    private float[] getDrawableBounds() {
        for (int i = 0; i < drawableBounds.length; i++) {
            drawableBounds[i] = 0;
        }
        drawableWidth = (drawableWidth == 0 ? width / 2f : drawableWidth);
        drawableHeight = (drawableHeight == 0 ? height / 2f : drawableHeight);
        switch (stateDrawableMode) {
            case LEFT: // left
                drawableBounds[0] = 0 + drawablePaddingLeft;
                drawableBounds[1] = height / 2f - drawableHeight / 2f + drawablePaddingTop;
                drawableBounds[2] = drawableBounds[0] + drawableWidth;
                drawableBounds[3] = drawableBounds[1] + drawableHeight;
                break;
            case TOP: // top
                drawableBounds[0] = width / 2f - drawableWidth / 2f + drawablePaddingLeft;
                drawableBounds[1] = 0 + drawablePaddingTop;
                drawableBounds[2] = drawableBounds[0] + drawableWidth;
                drawableBounds[3] = drawableBounds[1] + drawableHeight;
                break;
            case RIGHT: // right
                drawableBounds[0] = width - drawableWidth + drawablePaddingLeft;
                drawableBounds[1] = height / 2 - drawableHeight / 2 + drawablePaddingTop;
                drawableBounds[2] = drawableBounds[0] + drawableWidth;
                drawableBounds[3] = drawableBounds[1] + drawableHeight;
                break;
            case BOTTOM: // bottom
                drawableBounds[0] = width / 2f - drawableWidth / 2f + drawablePaddingLeft;
                drawableBounds[1] = height - drawableHeight + drawablePaddingTop;
                drawableBounds[2] = drawableBounds[0] + drawableWidth;
                drawableBounds[3] = drawableBounds[1] + drawableHeight;
                break;
            case CENTER: // center
                drawableBounds[0] = width / 2f - drawableWidth / 2f + drawablePaddingLeft;
                drawableBounds[1] = height / 2 - drawableHeight / 2 + drawablePaddingTop;
                drawableBounds[2] = drawableBounds[0] + drawableWidth;
                drawableBounds[3] = drawableBounds[1] + drawableHeight;
                break;
            case FILL: // fill
                drawableBounds[0] = 0;
                drawableBounds[1] = 0;
                drawableBounds[2] = width;
                drawableBounds[3] = height;
                break;
            case LEFT_TOP: // leftTop
                drawableBounds[0] = 0 + drawablePaddingLeft;
                drawableBounds[1] = 0 + drawablePaddingTop;
                drawableBounds[2] = drawableBounds[0] + drawableWidth;
                drawableBounds[3] = drawableBounds[1] + drawableHeight;
                break;
            case RIGHT_TOP: // rightTop
                drawableBounds[0] = width - drawableWidth + drawablePaddingLeft;
                drawableBounds[1] = 0 + drawablePaddingTop;
                drawableBounds[2] = drawableBounds[0] + drawableWidth;
                drawableBounds[3] = drawableBounds[1] + drawableHeight;
                break;
            case LEFT_BOTTOM: // leftBottom
                drawableBounds[0] = 0 + drawablePaddingLeft;
                drawableBounds[1] = height - drawableHeight + drawablePaddingTop;
                drawableBounds[2] = drawableBounds[0] + drawableWidth;
                drawableBounds[3] = drawableBounds[1] + drawableHeight;
                break;
            case RIGHT_BOTTOM: // rightBottom
                drawableBounds[0] = width - drawableWidth + drawablePaddingLeft;
                drawableBounds[1] = height - drawableHeight + drawablePaddingTop;
                drawableBounds[2] = drawableBounds[0] + drawableWidth;
                drawableBounds[3] = drawableBounds[1] + drawableHeight;
                break;
        }

        return drawableBounds;
    }

    private float[] getDrawable2Bounds() {
        for (int i = 0; i < drawable2Bounds.length; i++) {
            drawable2Bounds[i] = 0;
        }
        drawable2Width = (drawable2Width == 0 ? width / 2f : drawable2Width);
        drawable2Height = (drawable2Height == 0 ? height / 2f : drawable2Height);
        switch (stateDrawable2Mode) {
            case LEFT: // left
                drawable2Bounds[0] = 0 + drawable2PaddingLeft;
                drawable2Bounds[1] = height / 2f - drawable2Height / 2f + drawable2PaddingTop;
                drawable2Bounds[2] = drawable2Bounds[0] + drawable2Width;
                drawable2Bounds[3] = drawable2Bounds[1] + drawable2Height;
                break;
            case TOP: // top
                drawable2Bounds[0] = width / 2f - drawable2Width / 2f + drawable2PaddingLeft;
                drawable2Bounds[1] = 0 + drawable2PaddingTop;
                drawable2Bounds[2] = drawable2Bounds[0] + drawable2Width;
                drawable2Bounds[3] = drawable2Bounds[1] + drawable2Height;
                break;
            case RIGHT: // right
                drawable2Bounds[0] = width - drawable2Width + drawable2PaddingLeft;
                drawable2Bounds[1] = height / 2 - drawable2Height / 2 + drawable2PaddingTop;
                drawable2Bounds[2] = drawable2Bounds[0] + drawable2Width;
                drawable2Bounds[3] = drawable2Bounds[1] + drawable2Height;
                break;
            case BOTTOM: // bottom
                drawable2Bounds[0] = width / 2f - drawable2Width / 2f + drawable2PaddingLeft;
                drawable2Bounds[1] = height - drawable2Height + drawable2PaddingTop;
                drawable2Bounds[2] = drawable2Bounds[0] + drawable2Width;
                drawable2Bounds[3] = drawable2Bounds[1] + drawable2Height;
                break;
            case CENTER: // center
                drawable2Bounds[0] = width / 2f - drawable2Width / 2f + drawable2PaddingLeft;
                drawable2Bounds[1] = height / 2 - drawable2Height / 2 + drawable2PaddingTop;
                drawable2Bounds[2] = drawable2Bounds[0] + drawable2Width;
                drawable2Bounds[3] = drawable2Bounds[1] + drawable2Height;
                break;
            case FILL: // fill
                drawable2Bounds[0] = 0;
                drawable2Bounds[1] = 0;
                drawable2Bounds[2] = width;
                drawable2Bounds[3] = height;
                break;
            case LEFT_TOP: // leftTop
                drawable2Bounds[0] = 0 + drawable2PaddingLeft;
                drawable2Bounds[1] = 0 + drawable2PaddingTop;
                drawable2Bounds[2] = drawable2Bounds[0] + drawable2Width;
                drawable2Bounds[3] = drawable2Bounds[1] + drawable2Height;
                break;
            case RIGHT_TOP: // rightTop
                drawable2Bounds[0] = width - drawable2Width + drawable2PaddingLeft;
                drawable2Bounds[1] = 0 + drawable2PaddingTop;
                drawable2Bounds[2] = drawable2Bounds[0] + drawable2Width;
                drawable2Bounds[3] = drawable2Bounds[1] + drawable2Height;
                break;
            case LEFT_BOTTOM: // leftBottom
                drawable2Bounds[0] = 0 + drawable2PaddingLeft;
                drawable2Bounds[1] = height - drawable2Height + drawable2PaddingTop;
                drawable2Bounds[2] = drawable2Bounds[0] + drawable2Width;
                drawable2Bounds[3] = drawable2Bounds[1] + drawable2Height;
                break;
            case RIGHT_BOTTOM: // rightBottom
                drawable2Bounds[0] = width - drawable2Width + drawable2PaddingLeft;
                drawable2Bounds[1] = height - drawable2Height + drawable2PaddingTop;
                drawable2Bounds[2] = drawable2Bounds[0] + drawable2Width;
                drawable2Bounds[3] = drawable2Bounds[1] + drawable2Height;
                break;
        }

        return drawable2Bounds;
    }


    private void isNeedToAdjust(Canvas canvas, Adjuster.Opportunity currentOpportunity) {

        for (int i = 0; i < adjusterList.size(); i++) {
            Adjuster adjuster = adjusterList.get(i);
            if (currentOpportunity == adjuster.getOpportunity()) {
                if (adjuster.getType() == Adjuster.TYPE_SYSTEM) {
                    adjuster.adjust(this, canvas);
                } else if (autoAdjust) {
                    adjuster.adjust(this, canvas);
                }
            }
        }

    }

    /**
     * 获取SuperTextView的圆角大小
     *
     * @return 获取Corner值。默认为0。
     */
    public float getCorner() {
        return corner;
    }


    /**
     * 设置圆角大小
     *
     * @param corner 圆角大小，默认值为0。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setCorner(float corner) {
        this.corner = corner;
        postInvalidate();

        return this;
    }

    /**
     * 获取填充颜色
     *
     * @return 返回控件填充颜色。
     */
    public int getSolid() {
        return solid;
    }

    /**
     * 设置填充颜色
     *
     * @param solid 控件填充颜色, 默认为{@link Color#TRANSPARENT}。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setSolid(int solid) {
        this.solid = solid;
        postInvalidate();

        return this;
    }

    /**
     * 获取控件边框宽度
     *
     * @return 返回控件边框的宽度。
     */
    public float getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * 设置控件边框宽度
     *
     * @param strokeWidth 描边宽度。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        postInvalidate();

        return this;
    }

    /**
     * 获取边框颜色
     *
     * @return 返回描边颜色。默认为{@link Color#BLACK}。
     */
    public int getStrokeColor() {
        return strokeColor;
    }

    /**
     * 设置边框颜色
     *
     * @param strokeColor 描边颜色。默认为{@link Color#BLACK}。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        postInvalidate();

        return this;
    }

    /**
     * 该方法在后续版本将会被移除，请尽快使用{@link SuperTextView#addAdjuster(Adjuster)}来添加一个Adjuster。
     *
     * @param adjuster 添加一个Adjuster。{@link SuperTextView#addAdjuster(Adjuster)}
     *                 注意最多支持添加3个Adjuster，否则新的Adjuster总是会覆盖最后一个Adjuster。
     *                 {@link Adjuster}。会触发一次重绘。
     * @return SuperTextView
     */
    @Deprecated
    public SuperTextView setAdjuster(Adjuster adjuster) {

        return addAdjuster(adjuster);
    }

    /**
     * 获取最后一个 {@link Adjuster}
     *
     * @return 获得最后一个 {@link Adjuster}，如果存在的话。
     */
    public Adjuster getAdjuster() {
        if (adjusterList.size() > SYSTEM_ADJUSTER_SIZE) {
            return adjusterList.get(adjusterList.size() - 1);
        }
        return null;
    }

    /**
     * 添加一个Adjuster。
     * 注意，最多支持添加3个Adjuster，否则新的Adjuster总是会覆盖最后一个Adjuster。
     *
     * @param adjuster {@link Adjuster}。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView addAdjuster(Adjuster adjuster) {
        if (adjusterList.size() < SYSTEM_ADJUSTER_SIZE + ALLOW_CUSTOM_ADJUSTER_SIZE) {
            adjusterList.add(adjuster);
        } else {
            adjusterList.remove(adjusterList.size() - 1);
            adjusterList.add(adjuster);
        }
        postInvalidate();
        return this;
    }

    public SuperTextView addAdjuster(Adjuster adjuster, int index) {
        adjusterList.add(index, adjuster);


        return this;
    }

    private void addSysAdjuster(Adjuster adjuster) {
        if (adjuster != null) {
            adjuster.setType(Adjuster.TYPE_SYSTEM);
            adjusterList.add(SYSTEM_ADJUSTER_SIZE, adjuster);
            SYSTEM_ADJUSTER_SIZE++;
        }
    }

    /**
     * 移除指定位置的Adjuster。
     *
     * @param index 期望移除的Adjuster的位置。
     * @return 被移除的Adjuster，如果参数错误返回null。
     */
    public Adjuster removeAdjuster(int index) {
        int realIndex = SYSTEM_ADJUSTER_SIZE + index;
        if (realIndex > SYSTEM_ADJUSTER_SIZE - 1 && realIndex < adjusterList.size()) {
            Adjuster remove = adjusterList.remove(realIndex);
            postInvalidate();
            return remove;
        }
        return null;
    }

    /**
     * 移除指定的Adjuster，如果包含的话。
     *
     * @param adjuster 需要被移除的Adjuster
     * @return 被移除Adjuster在移除前在Adjuster列表中的位置。如果没有包含，返回-1。
     */
    public int removeAdjuster(Adjuster adjuster) {
        if (adjuster.type != Adjuster.TYPE_SYSTEM && adjusterList.contains(adjuster)) {
            int index = adjusterList.indexOf(adjuster);
            adjusterList.remove(adjuster);
            postInvalidate();
            return index;
        }
        return -1;
    }

    /**
     * 获得index对应的 {@link Adjuster}。
     *
     * @param index 期望获得的Adjuster的index。
     * @return index对应的Adjuster，如果参数错误返回null。
     */
    public Adjuster getAdjuster(int index) {
        int realIndex = SYSTEM_ADJUSTER_SIZE + index;
        if (realIndex > SYSTEM_ADJUSTER_SIZE - 1 && realIndex < adjusterList.size()) {
            return adjusterList.remove(realIndex);
        }
        return null;
    }

    /**
     * 检查是否开启了文字描边
     *
     * @return true 表示开启了文字描边，否则表示没开启。
     */
    public boolean isTextStroke() {
        return textStroke;
    }

    /**
     * 设置是否开启文字描边。
     * 注意，开启文字描边后，文字颜色需要通过 {@link #setTextFillColor(int)} 设置。
     *
     * @param textStroke true表示开启文字描边。默认为false。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setTextStroke(boolean textStroke) {
        this.textStroke = textStroke;
        postInvalidate();

        return this;
    }

    /**
     * 获取文字描边的颜色
     *
     * @return 文字描边的颜色。
     */
    public int getTextStrokeColor() {
        return textStrokeColor;
    }

    /**
     * 设置文字描边的颜色
     *
     * @param textStrokeColor 设置文字描边的颜色。默认为{@link Color#BLACK}。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setTextStrokeColor(int textStrokeColor) {
        this.textStrokeColor = textStrokeColor;
        postInvalidate();

        return this;
    }

    /**
     * 获取文字的填充颜色，在开启文字描边时 {@link #setTextStroke(boolean)} 默认为BLACK。
     *
     * @return 文字填充颜色。
     */
    public int getTextFillColor() {
        return textFillColor;
    }

    /**
     * 设置文字的填充颜色，需要开启文字描边 {@link #setTextStroke(boolean)} 才能生效。默认为BLACK。
     *
     * @param textFillColor 设置文字填充颜色。默认为{@link Color#BLACK}。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setTextFillColor(int textFillColor) {
        this.textFillColor = textFillColor;
        postInvalidate();

        return this;
    }

    /**
     * 获取文字描边的宽度
     *
     * @return 文字描边宽度。
     */
    public float getTextStrokeWidth() {
        return textStrokeWidth;
    }

    /**
     * 设置文字描边的宽度，需要开启文字描边 {@link #setTextStroke(boolean)} 才能生效。
     *
     * @param textStrokeWidth 设置文字描边宽度。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setTextStrokeWidth(float textStrokeWidth) {
        this.textStrokeWidth = textStrokeWidth;
        postInvalidate();

        return this;
    }

    /**
     * 检查是否开启 {@link Adjuster} 功能。
     *
     * @return true表示开启了Adjuster功能。
     */
    public boolean isAutoAdjust() {
        return autoAdjust;
    }

    /**
     * 设置是否开启 {@link Adjuster} 功能。
     *
     * @param autoAdjust true开启Adjuster功能。反之，关闭。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setAutoAdjust(boolean autoAdjust) {
        this.autoAdjust = autoAdjust;
        postInvalidate();

        return this;
    }

    /**
     * 检查左上角是否设置成了圆角。
     *
     * @return true表示左上角为圆角。
     */
    public boolean isLeftTopCornerEnable() {
        return leftTopCornerEnable;
    }

    /**
     * 设置左上角是否为圆角，可以单独控制SuperTextView的每一个圆角。
     *
     * @param leftTopCornerEnable true左上角圆角化。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setLeftTopCornerEnable(boolean leftTopCornerEnable) {
        this.leftTopCornerEnable = leftTopCornerEnable;
        postInvalidate();

        return this;
    }

    /**
     * 检查右上角是否设置成了圆角。
     *
     * @return true表示右上角为圆角。
     */
    public boolean isRightTopCornerEnable() {
        return rightTopCornerEnable;
    }

    /**
     * 设置右上角是否为圆角，可以单独控制SuperTextView的每一个圆角。
     *
     * @param rightTopCornerEnable true右上角圆角化。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setRightTopCornerEnable(boolean rightTopCornerEnable) {
        this.rightTopCornerEnable = rightTopCornerEnable;
        postInvalidate();

        return this;
    }

    /**
     * 检查左下角是否设置成了圆角。
     *
     * @return true表示左下角为圆角。
     */
    public boolean isLeftBottomCornerEnable() {
        return leftBottomCornerEnable;
    }

    /**
     * 设置左下角是否为圆角，可以单独控制SuperTextView的每一个圆角。
     *
     * @param leftBottomCornerEnable true左下角圆角化。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setLeftBottomCornerEnable(boolean leftBottomCornerEnable) {
        this.leftBottomCornerEnable = leftBottomCornerEnable;
        postInvalidate();

        return this;
    }

    /**
     * 检查右下角是否设置成了圆角。
     *
     * @return true表示右下角为圆角。
     */
    public boolean isRightBottomCornerEnable() {
        return rightBottomCornerEnable;
    }

    /**
     * 设置右下角是否为圆角，可以单独控制SuperTextView的每一个圆角。
     *
     * @param rightBottomCornerEnable true右下角圆角化。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setRightBottomCornerEnable(boolean rightBottomCornerEnable) {
        this.rightBottomCornerEnable = rightBottomCornerEnable;
        postInvalidate();

        return this;
    }

    /**
     * 获取状态图，当状态图通过 {@link #setDrawableAsBackground(boolean)} 设置为背景时，获取的就是背景图了。
     *
     * @return 状态图。
     */
    public Drawable getDrawable() {
        return drawable;
    }

    /**
     * 获取状态图2号。
     *
     * @return 状态图2。
     */
    public Drawable getDrawable2() {
        return drawable2;
    }

    /**
     * 设置状态图。需要调用 {@link #setShowState(boolean)} 才能显示。会触发一次重绘。
     * 当通过 {@link #setDrawableAsBackground(boolean)} 将状态图设置为背景后，将会被作为SuperTextView的背景图。
     * 通过 {@link #isDrawableAsBackground()} 来检查状态图是否被设置成了背景。
     *
     * @param drawable 状态图。
     * @return SuperTextView
     */
    public SuperTextView setDrawable(Drawable drawable) {
        this.drawable = drawable;
        drawableBackgroundShader = null;
        postInvalidate();

        return this;
    }

    /**
     * 设置状态图2。需要调用{@link #setShowState2(boolean)}才能显示。会触发一次重绘。
     *
     * @param drawable 状态图
     * @return SuperTextView
     */
    public SuperTextView setDrawable2(Drawable drawable) {
        this.drawable2 = drawable;
        postInvalidate();

        return this;
    }

    /**
     * 使用drawable资源设置状态图。需要调用{@link #setShowState(boolean)}才能显示。会触发一次重绘。
     *
     * @param drawableRes 状态图的资源id
     * @return SuperTextView
     */
    public SuperTextView setDrawable(int drawableRes) {
        this.drawable = getResources().getDrawable(drawableRes);
        drawableBackgroundShader = null;
        postInvalidate();

        return this;
    }

    /**
     * 使用drawable资源设置状态图2。需要调用{@link #setShowState2(boolean)}才能显示。会触发一次重绘。
     *
     * @param drawableRes 状态图的资源id
     * @return SuperTextView
     */
    public SuperTextView setDrawable2(int drawableRes) {
        this.drawable2 = getResources().getDrawable(drawableRes);
        postInvalidate();

        return this;
    }

    /**
     * 检查是否显示状态图
     *
     * @return 返回true，如果当前显示状态图。
     */
    public boolean isShowState() {
        return isShowState;
    }


    /**
     * 设置是否显示状态图。
     *
     * @param showState true，表示显示状态图。反之，不显示。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setShowState(boolean showState) {
        isShowState = showState;
        postInvalidate();

        return this;
    }

    /**
     * 检查当前状态图是否被作为SuperTextView的背景图。
     *
     * @return 当前Drawable是否作为SuperTextView的背景图。
     */
    public boolean isDrawableAsBackground() {
        return drawableAsBackground;
    }


    /**
     * 设置是否将状态图作为SuperTextView的背景图。
     * 将状态图设置为背景图，可以将SuperTextView变成一个ImageView用来展示图片，对SuperTextView设置的圆角、边框仍然对图片
     * 有效，这对于需要实现圆形图片、给图片加边框很有帮助。而且通过 {@link #setUrlImage(String)} 和 {@link #setUrlImage(String, boolean)}
     * 可以使得SuperTextView能够自动下载网络图片，然后进行展示。
     *
     * @param drawableAsBackground true，表示将Drawable作为背景图，其余所有对drawable的设置都会失效，直到该项为false。
     * @return SuperTextView
     */
    public SuperTextView setDrawableAsBackground(boolean drawableAsBackground) {
        this.drawableAsBackground = drawableAsBackground;
        if (!drawableAsBackground) {
            drawableBackgroundShader = null;
        }
        postInvalidate();
        return this;
    }

    /**
     * 检查是否显示状态图2。
     *
     * @return 返回true，如果当前显示状态图2。
     */
    public boolean isShowState2() {
        return isShowState2;
    }


    /**
     * 设置是否显示状态图2
     *
     * @param showState true，表示显示状态图2。反之，不显示。会触发一次重绘。
     * @return SuperTextView
     */
    public SuperTextView setShowState2(boolean showState) {
        isShowState2 = showState;
        postInvalidate();

        return this;
    }

    /**
     * 获取状态图的显示模式。在 {@link DrawableMode} 中查看所有支持的模式。
     *
     * @return 状态图显示模式。{@link DrawableMode}
     */
    public DrawableMode getStateDrawableMode() {
        return stateDrawableMode;
    }

    /**
     * 获取状态图2的显示模式。在 {@link DrawableMode} 中查看所有支持的模式。
     *
     * @return 状态图2显示模式。{@link DrawableMode}
     */
    public DrawableMode getStateDrawable2Mode() {
        return stateDrawable2Mode;
    }

    /**
     * 设置状态图显示模式。默认为{@link DrawableMode#CENTER}。会触发一次重绘。
     * 在 {@link DrawableMode} 中查看所有支持的模式。
     *
     * @param stateDrawableMode 状态图显示模式
     * @return SuperTextView
     */
    public SuperTextView setStateDrawableMode(DrawableMode stateDrawableMode) {
        this.stateDrawableMode = stateDrawableMode;
        postInvalidate();

        return this;
    }

    /**
     * 设置状态图2显示模式。默认为{@link DrawableMode#CENTER}。会触发一次重绘。
     * 在 {@link DrawableMode} 中查看所有支持的模式。
     *
     * @param stateDrawableMode 状态图2显示模式
     * @return SuperTextView
     */
    public SuperTextView setStateDrawable2Mode(DrawableMode stateDrawableMode) {
        this.stateDrawable2Mode = stateDrawableMode;
        postInvalidate();

        return this;
    }

    /**
     * 获取状态图的宽度。
     *
     * @return 状态图的宽度。
     */
    public float getDrawableWidth() {
        return drawableWidth;
    }

    /**
     * 获取状态图2的宽度。
     *
     * @return 状态图2的宽度。
     */
    public float getDrawable2Width() {
        return drawable2Width;
    }

    /**
     * 设置状态图宽度。默认为控件的1／2。会触发一次重绘。
     *
     * @param drawableWidth 状态图宽度
     * @return SuperTextView
     */
    public SuperTextView setDrawableWidth(float drawableWidth) {
        this.drawableWidth = drawableWidth;
        postInvalidate();

        return this;
    }

    /**
     * 设置状态图2宽度。默认为控件的1／2。会触发一次重绘。
     *
     * @param drawableWidth 状态图2宽度
     * @return SuperTextView
     */
    public SuperTextView setDrawable2Width(float drawableWidth) {
        this.drawable2Width = drawableWidth;
        postInvalidate();

        return this;
    }

    /**
     * 获取状态图的高度
     *
     * @return 状态图的高度。
     */
    public float getDrawableHeight() {
        return drawableHeight;
    }

    /**
     * 获取状态图2的高度
     *
     * @return 状态图2的高度。
     */
    public float getDrawable2Height() {
        return drawable2Height;
    }

    /**
     * 设置状态图高度。默认为控件的1／2。会触发一次重绘。
     *
     * @param drawableHeight 状态图高度
     * @return SuperTextView
     */
    public SuperTextView setDrawableHeight(float drawableHeight) {
        this.drawableHeight = drawableHeight;
        postInvalidate();

        return this;
    }

    /**
     * 设置状态图2高度。默认为控件的1／2。会触发一次重绘。
     *
     * @param drawableHeight 状态图2高度
     * @return SuperTextView
     */
    public SuperTextView setDrawable2Height(float drawableHeight) {
        this.drawable2Height = drawableHeight;
        postInvalidate();

        return this;
    }

    /**
     * 获取状态图相对于控件左边的边距。
     *
     * @return 状态图左边距
     */
    public float getDrawablePaddingLeft() {
        return drawablePaddingLeft;
    }

    /**
     * 获取状态图2相对于控件左边的边距。
     *
     * @return 状态图2左边距
     */
    public float getDrawable2PaddingLeft() {
        return drawable2PaddingLeft;
    }

    /**
     * 设置状态图相对于控件左边的边距。会触发一次重绘。
     *
     * @param drawablePaddingLeft 状态图左边距。
     * @return SuperTextView
     */
    public SuperTextView setDrawablePaddingLeft(float drawablePaddingLeft) {
        this.drawablePaddingLeft = drawablePaddingLeft;
        postInvalidate();

        return this;
    }

    /**
     * 设置状态图2相对于控件左边的边距。会触发一次重绘。
     *
     * @param drawablePaddingLeft 状态图左边距。
     * @return SuperTextView
     */
    public SuperTextView setDrawable2PaddingLeft(float drawablePaddingLeft) {
        this.drawable2PaddingLeft = drawable2PaddingLeft;
        postInvalidate();

        return this;
    }

    /**
     * 获取状态图相对于控件上边的边距。
     *
     * @return 状态图上边距。
     */
    public float getDrawablePaddingTop() {
        return drawablePaddingTop;
    }

    /**
     * 获取状态图2相对于控件上边的边距。
     *
     * @return 状态图2上边距。
     */
    public float getDrawable2PaddingTop() {
        return drawable2PaddingTop;
    }

    /**
     * 设置状态图相对于控件上边的边距。会触发一次重绘。
     *
     * @param drawablePaddingTop 状态图上边距。
     * @return SuperTextView
     */
    public SuperTextView setDrawablePaddingTop(float drawablePaddingTop) {
        this.drawablePaddingTop = drawablePaddingTop;
        postInvalidate();
        return this;
    }

    /**
     * 设置状态图2相对于控件上边的边距。会触发一次重绘。
     *
     * @param drawablePaddingTop 状态图2上边距。
     * @return SuperTextView
     */
    public SuperTextView setDrawable2PaddingTop(float drawablePaddingTop) {
        this.drawable2PaddingTop = drawablePaddingTop;
        postInvalidate();
        return this;
    }

    /**
     * 获取渐变色的起始颜色。
     *
     * @return 渐变起始色。
     */
    public int getShaderStartColor() {
        return shaderStartColor;
    }

    /**
     * 设置渐变起始色。需要调用{@link SuperTextView#setShaderEnable(boolean)}后才能生效。会触发一次重绘。
     *
     * @param shaderStartColor 渐变起始色
     * @return SuperTextView
     */
    public SuperTextView setShaderStartColor(int shaderStartColor) {
        this.shaderStartColor = shaderStartColor;
        shader = null;
        postInvalidate();
        return this;
    }

    /**
     * 获取渐变色的结束颜色。
     *
     * @return 渐变结束色。
     */
    public int getShaderEndColor() {
        return shaderEndColor;
    }

    /**
     * 设置渐变结束色。需要调用{@link SuperTextView#setShaderEnable(boolean)}后才能生效。会触发一次重绘。
     *
     * @param shaderEndColor 渐变结束色
     * @return SuperTextView
     */
    public SuperTextView setShaderEndColor(int shaderEndColor) {
        this.shaderEndColor = shaderEndColor;
        shader = null;
        postInvalidate();
        return this;
    }

    /**
     * 获取渐变色模式。在{@link ShaderMode}中可以查看所有支持的模式。
     * 需要调用{@link SuperTextView#setShaderEnable(boolean)}后才能生效。
     *
     * @return 渐变模式。
     */
    public ShaderMode getShaderMode() {
        return shaderMode;
    }

    /**
     * 设置渐变模式。在{@link ShaderMode}中可以查看所有支持的模式。
     * 需要调用{@link SuperTextView#setShaderEnable(boolean)}后才能生效。
     *
     * @param shaderMode 渐变模式
     * @return SuperTextView
     */
    public SuperTextView setShaderMode(ShaderMode shaderMode) {
        this.shaderMode = shaderMode;
        shader = null;
        postInvalidate();
        return this;
    }

    /**
     * 检查是否启用了渐变功能。
     *
     * @return 返回true，如果启用了渐变功能。
     */
    public boolean isShaderEnable() {
        return shaderEnable;
    }

    /**
     * 设置是否启用渐变色功能。
     *
     * @param shaderEnable true启用渐变功能。反之，停用。
     * @return SuperTextView
     */
    public SuperTextView setShaderEnable(boolean shaderEnable) {
        this.shaderEnable = shaderEnable;
        postInvalidate();
        return this;
    }

    /**
     * 获得当前按压背景色。没有设置默认为Color.TRANSPARENT。
     *
     * @return 按压时的背景色
     */
    public int getPressBgColor() {
        return pressBgColor;
    }

    /**
     * 获得当前按压背景色。一旦设置，立即生效。
     * 取消可以设置Color.TRANSPARENT。
     *
     * @param pressBgColor 按压背景色
     */
    public SuperTextView setPressBgColor(int pressBgColor) {
        this.pressBgColor = pressBgColor;
        return this;
    }

    /**
     * 获得当前按压文字颜色色。没有设置默认为-99。
     *
     * @return 按压时文字颜色
     */
    public int getPressTextColor() {
        return pressTextColor;
    }

    /**
     * 获得当前按压文字色。一旦设置，立即生效。
     * 取消可以设置-99。
     *
     * @param pressTextColor 按压时文字颜色
     */
    public SuperTextView setPressTextColor(int pressTextColor) {
        this.pressTextColor = pressTextColor;
        return this;
    }

    /**
     * 获取当前SuperTextView在播放 {@link Adjuster} 时的帧率。默认为60fps
     *
     * @return 帧率
     */
    public int getFrameRate() {
        return frameRate;
    }

    /**
     * 设置帧率，即每秒帧数。可在动画过程中随时改变。默认为60fps
     *
     * @param frameRate 帧率
     * @return SuperTextView
     */
    public SuperTextView setFrameRate(int frameRate) {
        if (frameRate > 0) {
            this.frameRate = frameRate;
        } else {
            this.frameRate = 60;
        }
        return this;
    }

    /**
     * 将一个网络图片作为SuperTextView的StateDrawable。
     * 在调用这个函数前，建议开发者根据当前所使用的图片框架实现{@link com.library.image_engine.Engine}，
     * 然后通过{@link ImageEngine#install(Engine)}为SuperTextView的ImageEngine安装一个全局引擎，开发者可以在
     * {@link Application#onCreate()}中进行配置（需要注意任何时候新安装的引擎总会替换掉原本的引擎）。
     * 在未设置引擎的情况下，SuperTextView仍然会通过内置的一个十分简易引擎去下载图片。
     *
     * @param url          网络图片地址
     * @param asBackground 是否将下载的图片作为Background。
     *                     - false表示下载好的图片将作为SuperTextView的StateDrawable
     *                     - true表示将下载好的图片作为背景，效果和{@link SuperTextView#setDrawableAsBackground(boolean)}
     *                     是一样的。
     * @return SuperTextView
     */
    public SuperTextView setUrlImage(final String url, final boolean asBackground) {
        // 检查是否已经安装了Engine，没有安装会安装一个默认的，后面仍然可以随时被替换
        ImageEngine.checkEngine();
        // 缓存当前的imageUrl，当下载完成后需要校验
        curImageUrl = url;
        ImageEngine.load(url, new ImageEngine.Callback() {
            @Override
            public void onCompleted(final Drawable drawable) {
                if (getContext() != null && drawable != null && TextUtils.equals(curImageUrl, url)) {
                    SuperTextView.this.drawable = drawable;
                    isShowState = !asBackground;
                    setDrawableAsBackground(asBackground);
                    // if (STVUtils.isOnMainThread()) {
                    // SuperTextView.this.drawable = drawable;
                    // setDrawableAsBackground(asBackground);
                    // } else {
                    // post(new Runnable() {
                    // @Override
                    // public void run() {
                    // if (getContext() != null) {
                    // SuperTextView.this.drawable = drawable;
                    // setDrawableAsBackground(asBackground);
                    // }
                    // }
                    // });
                    // }
                }
            }
        });
        return this;
    }

    /**
     * 将一个网络图片作为SuperTextView的StateDrawable，调用这个方法StateDrawable将会被设置为背景模式。
     * 在调用这个函数前，建议开发者根据当前所使用的图片框架实现{com.coorchice.library.image_engine.Engine}，
     * 然后通过{ ImageEngine#install(Engine)}为SuperTextView的ImageEngine安装一个全局引擎，开发者可以在
     * {@link Application#onCreate()}中进行配置（需要注意任何时候新安装的引擎总会替换掉原本的引擎）。
     * 在未设置引擎的情况下，SuperTextView仍然会通过内置的一个十分简易引擎去下载图片。
     *
     * @param url 网络图片地址
     * @return SuperTextView
     */
    public SuperTextView setUrlImage(final String url) {
        return setUrlImage(url, true);
    }


    /**
     * 启动动画。需要设置{@link SuperTextView#setAutoAdjust(boolean)}为true才能看到。
     */
    public void startAnim() {
        needRun = true;
        runnable = false;
        if (animThread == null) {
            checkWhetherNeedInitInvalidate();
            needRun = true;
            runnable = true;
            if (handleAnim == null) {
                initHandleAnim();
            }
            animThread = new Thread(handleAnim);
            animThread.start();
        }
    }

    private void initHandleAnim() {
        handleAnim = new Runnable() {
            @Override
            public void run() {
                while (runnable) {
                    synchronized (invalidate) {
                        post(invalidate);
                    }
                    try {
                        Thread.sleep(1000 / frameRate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        runnable = false;
                    }
                    // Log.e("SuperTextView", " -> startAnim: " + Thread.currentThread().getId() + "-> "
                    // + hashCode() + ": It's running!");
                }
                animThread = null;
                if (needRun) {
                    startAnim();
                }
            }
        };
    }

    private void checkWhetherNeedInitInvalidate() {
        if (invalidate == null) {
            invalidate = new Runnable() {
                @Override
                public void run() {
                    postInvalidate();
                }
            };
        }
    }

    /**
     * 停止动画。不能保证立即停止，但最终会停止。
     */
    public void stopAnim() {
        runnable = false;
        needRun = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean hasConsume = false;
        int action = event.getAction();
        int actionMasked = action & MotionEvent.ACTION_MASK;
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < adjusterList.size(); i++) {
                Adjuster adjuster = adjusterList.get(i);
                if (adjuster.onTouch(this, event)) {
                    if (adjuster.type == Adjuster.TYPE_SYSTEM || isAutoAdjust()) {
                        hasConsume = true;
                        touchAdjusters.add(adjuster);
                    }
                }
            }
            superTouchEvent = super.onTouchEvent(event);
        } else {
            for (int i = 0; i < touchAdjusters.size(); i++) {
                Adjuster adjuster = touchAdjusters.get(i);
                adjuster.onTouch(this, event);
                hasConsume = true;
            }
            if (superTouchEvent) {
                super.onTouchEvent(event);
            }
            if (actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_CANCEL) {
                touchAdjusters.clear();
                superTouchEvent = false;
            }
        }
        return hasConsume || superTouchEvent;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility != VISIBLE && visibility != INVISIBLE) {
            cacheRunnableState = runnable;
            cacheNeedRunState = needRun;
            stopAnim();
        } else if (cacheRunnableState && cacheNeedRunState) {
            startAnim();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnim();
        super.onDetachedFromWindow();
    }

    /**
     * Adjuster被设计用来在SuperTextView的绘制过程中插入一些操作。
     * 这具有非常重要的意义。你可以用它来实现各种各样的效果。比如插入动画、修改状态。
     * 你可以指定Adjuster的作用层级，通过调用{@link Adjuster#setOpportunity(Opportunity)}，
     * {@link Opportunity}。默认为{@link Opportunity#BEFORE_TEXT}。
     * 在Adjuster中，可以获取到控件的触摸事件，这对于实现一些复杂的交互效果很有帮助。
     */
    public static abstract class Adjuster {
        private static final int TYPE_SYSTEM = 0x001;
        private static final int TYPE_CUSTOM = 0x002;


        private Opportunity opportunity = Opportunity.BEFORE_TEXT;
        private int type = TYPE_CUSTOM;

        /**
         * 在Canvas上绘制的东西将能够呈现在SuperTextView上。
         * 提示：你需要注意图层的使用。
         *
         * @param v      SuperTextView
         * @param canvas 用于绘制的Canvas。注意对Canvas的变换最好使用图层，否则会影响后续的绘制。
         */
        protected abstract void adjust(SuperTextView v, Canvas canvas);

        /**
         * 在这个方法中，你能够捕获到SuperTextView中发生的触摸事件。
         * 需要注意，如果你在该方法中返回了true来处理SuperTextView的触摸事件的话，你将在
         * SuperTextView的setOnTouchListener中设置的OnTouchListener中同样能够捕获到这些触摸事件，即使你在OnTouchListener中返回了false。
         * 但是，如果你在OnTouchListener中返回了true，这个方法将会失效，因为事件在OnTouchListener中被拦截了。
         *
         * @param v     SuperTextView
         * @param event 控件件接收到的触摸事件。
         * @return 默认返回false。如果想持续的处理控件的触摸事件就返回true。否则，只能接收到{@link MotionEvent#ACTION_DOWN}事件。
         */
        public boolean onTouch(SuperTextView v, MotionEvent event) {
            return false;
        }

        ;

        /**
         * 获取当前Adjuster的层级。
         *
         * @return Adjuster的作用层级。
         */
        public Opportunity getOpportunity() {
            return opportunity;
        }

        /**
         * 设置Adjuster的作用层级。在 {@link Opportunity} 中可以查看所有支持的层级。
         *
         * @param opportunity Adjuster的作用层级
         * @return 返回Adjuster本身，方便调用。
         */
        public Adjuster setOpportunity(Opportunity opportunity) {
            this.opportunity = opportunity;
            return this;
        }

        /**
         * @hide
         */
        private Adjuster setType(int type) {
            this.type = type;
            return this;
        }

        private int getType() {
            return type;
        }

        /**
         * Adjuster贴心的设计了控制作用层级的功能。
         * 你可以通过{@link Adjuster#setOpportunity(Opportunity)}来指定Adjuster的绘制层级。
         * 在SuperTextView中，绘制层级被从下到上分为：背景层、Drawable层、文字层3个层级。
         * 通过Opportunity来指定你的Adjuster想要插入到那个层级间。
         */
        public static enum Opportunity {
            /**
             * 背景层和Drawable层之间
             */
            BEFORE_DRAWABLE,
            /**
             * Drawable层和文字层之间
             */
            BEFORE_TEXT,
            /**
             * 最顶层
             */
            AT_LAST
        }
    }

    /**
     * 状态图的显示模式。SuperTextView定义了10中显示模式。它们控制着状态图的相对位置。
     * 默认为居中，即{@link DrawableMode#CENTER}。
     */
    public static enum DrawableMode {
        /**
         * 正左
         */
        LEFT(0),
        /**
         * 正上
         */
        TOP(1),
        /**
         * 正右
         */
        RIGHT(2),
        /**
         * 正下
         */
        BOTTOM(3),
        /**
         * 居中
         */
        CENTER(4),
        /**
         * 充满整个控件
         */
        FILL(5),
        /**
         * 左上
         */
        LEFT_TOP(6),
        /**
         * 右上
         */
        RIGHT_TOP(7),
        /**
         * 左下
         */
        LEFT_BOTTOM(8),
        /**
         * 右下
         */
        RIGHT_BOTTOM(9);

        public int code;

        DrawableMode(int code) {
            this.code = code;
        }

        public static DrawableMode valueOf(int code) {
            for (DrawableMode mode : DrawableMode.values()) {
                if (mode.code == code) {
                    return mode;
                }
            }
            return CENTER;
        }
    }

    /**
     * SuperTextView的渐变模式。
     * 可以通过 {@link SuperTextView#setStateDrawableMode(DrawableMode)}设置控件的Shader模式
     */
    public static enum ShaderMode {
        /**
         * 从上到下
         */
        TOP_TO_BOTTOM(0),
        /**
         * 从下到上
         */
        BOTTOM_TO_TOP(1),
        /**
         * 从左到右
         */
        LEFT_TO_RIGHT(2),
        /**
         * 从右到左
         */
        RIGHT_TO_LEFT(3);

        public int code;

        ShaderMode(int code) {
            this.code = code;
        }

        public static ShaderMode valueOf(int code) {
            for (ShaderMode mode : ShaderMode.values()) {
                if (mode.code == code) {
                    return mode;
                }
            }
            return TOP_TO_BOTTOM;
        }
    }
}
