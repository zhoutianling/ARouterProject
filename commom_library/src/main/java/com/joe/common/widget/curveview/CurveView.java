package com.joe.common.widget.curveview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.joe.common.R;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lixindong on 9/23/16.
 */

public class CurveView extends View implements DataObserver {

    public CurveView(Context context) {
        this(context, null);
    }

    public CurveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    public static final String TAG = CurveView.class.getSimpleName();

    protected int mUnitWidth;
    protected int mFillColor;
    protected int mContentColor;
    protected int mStrokeWidth;
    protected int mContentPaddingTop;
    protected int mContentPaddingBottom;
    protected int mDotTextSize;
    protected int mDotTextColor;
    protected int mAxisTextSize;
    protected int mAxisTextColor;

    protected int mAxisLineToCurveAreaGapHeight;
    protected int mAxisTextToLineGapHeight;

    private int mCorner;

    protected int mContentPaddingStart;
    protected int mContentPaddingEnd;

    protected int mGravity = 0;

    private boolean mShowAll = false;

    /**
     * <flag name="top" value="0x01" />
     * <flag name="bottom" value="0x02" />
     * <flag name="start" value="0x04" />
     * <flag name="end" value="0x08" />
     * <flag name="center_vertical" value="0x10" />
     * <flag name="center_horizontal" value="0x20" />
     * <flag name="center" value="0x30" />
     */
    public static class Gravity {
        public final static int TOP = 0x01;
        public final static int BOTTOM = 0x02;
        public final static int START = 0x04;
        public final static int END = 0x08;
        public final static int CENTER_VERTICAL = 0x10;
        public final static int CENTER_HORIZONTAL = 0x20;
        public final static int CENTER = 0x30;
    }

    private boolean mShowXLine = false;
    private boolean mShowXText = false;
    private boolean mShowY = false;

    protected Paint mContentPaint;
    protected Paint mBackgroundPaint;
    protected TextPaint mXAxisPaint;
    protected TextPaint mDotTextPaint;
    private Paint mLinePaint;
    private Drawable mBackground;

    protected int mOffsetX = 0;

    protected Path mContentPath;
    protected CornerPathEffect mCornerPathEffect;
    private int[] colors;
    PaintFlagsDrawFilter mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    private void init() {
        colors = new int[]{-456123, -753951, -526398, -1102156};
        mCornerPathEffect = new CornerPathEffect(mCorner);

        mContentPaint = new Paint();
        mContentPaint.setStyle(Paint.Style.STROKE);
        mContentPaint.setColor(mContentColor);
        mContentPaint.setStrokeWidth(mStrokeWidth);
        mContentPaint.setPathEffect(mCornerPathEffect);

        mBackgroundPaint = new Paint();
//        mBackgroundPaint.setColor(mFillColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mXAxisPaint = new TextPaint();
        mXAxisPaint.setColor(mAxisTextColor);
        mXAxisPaint.setTextSize(mAxisTextSize);

        mDotTextPaint = new TextPaint();
        mDotTextPaint.setColor(mDotTextColor);
        mDotTextPaint.setTextSize(mDotTextSize);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        mContentPath = new Path();
        //        mMaxVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mMaxVelocity = 3000;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Curve, 0, 0);
        try {
            mUnitWidth = a.getDimensionPixelSize(R.styleable.Curve_unitWidth, 120);
            mFillColor = a.getColor(R.styleable.Curve_backgroundColor, Color.TRANSPARENT);
            mContentColor = a.getColor(R.styleable.Curve_contentColor, Color.BLACK);
            mStrokeWidth = a.getDimensionPixelSize(R.styleable.Curve_strokeWidth, 10);
            mContentPaddingTop = a.getDimensionPixelSize(R.styleable.Curve_contentPaddingTop, 0);
            mContentPaddingBottom = a.getDimensionPixelSize(R.styleable.Curve_contentPaddingBottom, 0);
            mDotTextSize = a.getDimensionPixelSize(R.styleable.Curve_dotTextSize, 60);
            mDotTextColor = a.getColor(R.styleable.Curve_dotTextColor, Color.BLACK);
            mAxisTextSize = a.getDimensionPixelSize(R.styleable.Curve_axisTextSize, 40);
            mAxisTextColor = a.getColor(R.styleable.Curve_axisTextColor, Color.BLACK);

            mCorner = a.getDimensionPixelSize(R.styleable.Curve_corners, 0);

            mContentPaddingStart = a.getDimensionPixelSize(R.styleable.Curve_contentPaddingStart, 0);
            mContentPaddingEnd = a.getDimensionPixelSize(R.styleable.Curve_contentPaddingEnd, 0);

            mShowXLine = a.getBoolean(R.styleable.Curve_showXLine, false);
            mShowXText = a.getBoolean(R.styleable.Curve_showXText, false);
            mShowY = a.getBoolean(R.styleable.Curve_showY, false);

            mGravity = a.getInteger(R.styleable.Curve_dotTextGravity, 0);

            mShowAll = a.getBoolean(R.styleable.Curve_showAll, false);

            mAxisTextToLineGapHeight = a.getDimensionPixelSize(R.styleable.Curve_axisTextToLineGapHeight, 0);
            mAxisLineToCurveAreaGapHeight = a.getDimensionPixelSize(R.styleable.Curve_axisLineToCurveAreaGapHeight, 0);

        } finally {
            a.recycle();
        }
    }

    private int scannerStart = 5;
    private float step = 1;  //扫描线移动步进

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 设置抗锯齿
        canvas.setDrawFilter(mPaintFlagsDrawFilter);

        canvas.drawColor(mFillColor);
        drawBackground(canvas);
        drawLine(canvas);
        if (mDecorations == null || mDecorations.size() == 0) {
            return;
        }
        int curveAreaHeight = getHeight()
                - mContentPaddingTop
                - mContentPaddingBottom
                - mAxisLineToCurveAreaGapHeight
                - mAxisTextToLineGapHeight
                - mAxisTextSize
                - mStrokeWidth;
        int curveAreaTop = mContentPaddingTop;
        int curveAreaBottom = mContentPaddingTop + curveAreaHeight;
        int curveAreaStart = mContentPaddingStart;
        int curveAreaEnd = mContentPaddingEnd;

        int heightPerLevel = curveAreaHeight / (mMaxLevel - mMinLevel);

        float scaleX = 1f;
        int unitWidth = mUnitWidth;
        if (mShowAll) {
            unitWidth = (getWidth() - mContentPaddingStart - mContentPaddingEnd) / (mDecorations.size() - 1);
        }

        if (mContentPath.isEmpty() || mForceUpdate) {
            mForceUpdate = false;

            mContentPath.moveTo(0, curveAreaBottom - (mDecorations.get(0).mLevel - mMinLevel) * heightPerLevel);
            for (int i = 1; i < mDecorations.size(); i++) {
                mContentPath.lineTo(i * unitWidth * scaleX, curveAreaBottom - (mDecorations.get(i).mLevel - mMinLevel) * heightPerLevel);
            }
        }

        canvas.save();
        canvas.translate(mOffsetX + mContentPaddingStart, 0);

        canvas.drawPath(mContentPath, mContentPaint);

        for (int i = 0; i < mDecorations.size(); i++) {
            ItemDecoration decoration = mDecorations.get(i);

            int dotX = (int) (unitWidth * scaleX * i);
            int dotY = curveAreaBottom - (mDecorations.get(i).mLevel - mMinLevel) * heightPerLevel;

            // draw x axis text
            if (mShowXText) {
                int offsetX = getTextOffsetX(mXAxisPaint, decoration.mXAxisText, Gravity.CENTER_HORIZONTAL);
                canvas.drawText(decoration.mXAxisText, dotX + offsetX, getHeight() - mContentPaddingBottom, mXAxisPaint);
            }
            // draw mark text on dot
            for (Mark mark : decoration.mMarks) {
                int offsetX = getTextOffsetX(mDotTextPaint, mark.content, mark.gravity) + mark.marginStart - mark.marginEnd;
                int offsetY = getTextOffsetY(mDotTextPaint, mark.gravity) + mark.marginTop - mark.marginBottom;

                canvas.drawText(mark.content, dotX + offsetX, dotY + offsetY, mDotTextPaint);
            }
        }
        canvas.restore();
        int axisY = getHeight() - mContentPaddingBottom - mAxisTextToLineGapHeight - mAxisTextSize;
        if (mShowXLine) {
            canvas.drawLine(0,
                    0,
                    getWidth(),
                    0,
                    mContentPaint);
        }
    }

    private void drawBackground(Canvas canvas) {
        mBackgroundPaint.setShader(new LinearGradient(0, 0, getWidth(), getHeight(), colors, null, Shader.TileMode.REPEAT));
        canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);
    }

    private void drawLine(Canvas canvas) {
        if (scannerStart <= getWidth()) {
            canvas.drawLine(scannerStart, 0, scannerStart, getHeight(), mLinePaint);
            scannerStart += step;
        } else {
            scannerStart = mContentPaddingStart;
        }
        postInvalidate();
    }

    public void setColors(int[] colors) {
        this.colors = colors;
        this.invalidate();
    }

    public void setStep(float step) {
        this.step = step;
        this.invalidate();
    }

    private int getTextOffsetY(TextPaint paint, int gravity) {
        int height = (int) (paint.getFontMetrics().descent - paint.getFontMetrics().ascent);
        int offset = (int) (paint.getFontMetrics().descent + paint.getFontMetrics().ascent) / 2;
        if ((gravity & Gravity.CENTER_VERTICAL) != 0) {
            offset += height / 2;
        } else if ((gravity & Gravity.BOTTOM) != 0) {
            offset += height;
        }
        return offset;
    }

    private int getTextOffsetX(TextPaint paint, String s, int gravity) {
        int width = (int) paint.measureText(s);
        int offset = 0;
        if ((gravity & Gravity.CENTER_HORIZONTAL) != 0) {
            offset = -width / 2;
        } else if ((gravity & Gravity.START) != 0) {
            offset = -width;
        }

        return offset;
    }

    public void setOffsetX(int offsetX) {
        this.mOffsetX = offsetX;
    }

    int mLastX;
    VelocityTracker mVelocityTracker;
    int mMaxVelocity;

    private void acquireVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mShowAll) {
            return false;
        }
        acquireVelocityTracker(event);
        final VelocityTracker velocityTracker = mVelocityTracker;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                int offset = (int) (mOffsetX + (event.getRawX() - mLastX));
                offset = checkOffset(offset);
                setOffsetX(offset);
                mLastX = (int) event.getRawX();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityX = velocityTracker.getXVelocity();
                final int initialOffset = mOffsetX;
                ValueAnimator animator = ValueAnimator.ofFloat(velocityX, 0);
                final int duration = 300;
                animator.setDuration(duration);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float v = (Float) animation.getAnimatedValue();
                        int t = (int) animation.getCurrentPlayTime();
                        int d = (int) (velocityX * duration * 1.0f / 1000 / 2 - v * (duration - t) * 1.0f / 1000 / 2);
                        setOffsetX(checkOffset(initialOffset + d));
                        invalidate();
                    }
                });
                animator.start();
                break;
        }
        return true;
    }

    /**
     * offset > 0, scroll to left
     * offset < 0, scroll to right
     * normally, offset should <= 0
     *
     * @param offset offset to scroll horizontally
     * @return fixed offset, not to exceed limit
     */
    private int checkOffset(int offset) {
        // only scroll when paint width > view width
        int paintWdith = (mDecorations.size() - 1) * mUnitWidth + mContentPaddingStart + mContentPaddingEnd;
        if (paintWdith < getWidth()) {
            return mOffsetX;
        }

        if (offset > 0) {
            offset = 0;
        }
        if (offset < getWidth() - paintWdith) {
            offset = getWidth() - paintWdith;
        }
        return offset;
    }

    private Adapter mAdapter;

    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(this);
        }
        mAdapter = adapter;
        adapter.registerDataSetObserver(this);

        updateAdapterData();
    }

    @Override
    public void onChanged() {
        updateAdapterData();
        invalidate();
    }

    private int mMinLevel = 0;
    private int mMaxLevel = 100;
    private SparseArray<ItemDecoration> mDecorations = new SparseArray<>();

    private boolean mForceUpdate = false;


    private void updateAdapterData() {
        mForceUpdate = true;
        clearData();
        if (mAdapter == null) {
            return;
        }

        mMinLevel = mAdapter.getMinLevel();
        mMaxLevel = mAdapter.getMaxLevel();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            ItemDecoration decoration = new ItemDecoration();

            int level = mAdapter.getLevel(i);
            Set<Mark> marks = mAdapter.onCreateMarks(i);
            String xAxisText = mAdapter.getXAxisText(i);

            decoration.mLevel = level;
            decoration.mMarks = marks;
            decoration.mXAxisText = xAxisText;

            mAdapter.decorate(decoration, i);
            mDecorations.append(i, decoration);
        }
    }

    private void clearData() {
        // dot data
        // dot text data
        mDecorations.clear();
        mContentPath.reset();
        // line data
        // other data
    }

    public abstract static class Adapter {

        private final DataObservable mDataSetObservable = new DataObservable();

        public void registerDataSetObserver(DataObserver observer) {
            mDataSetObservable.registerObserver(observer);
        }

        public void unregisterDataSetObserver(DataObserver observer) {
            mDataSetObservable.unregisterObserver(observer);
        }

        /**
         * Notifies the attached observers that the underlying data has been changed
         * and any View reflecting the data set should refresh itself.
         */
        public void notifyDataSetChanged() {
            mDataSetObservable.notifyChanged();
        }

        /**
         * @return 点的数量
         */
        public abstract int getCount();

        /**
         * 绘制自定义元素, 未完成
         *
         * @param canvas
         * @param x
         * @param y
         * @param position
         */
        public void draw(Canvas canvas, int x, int y, int position) {
            // TODO
        }

        /**
         * 添加自定义数据, 未完成
         *
         * @param decoration
         * @param position
         */
        public void decorate(ItemDecoration decoration, int position) {
            // TODO
        }

        /**
         * 设置点上的文字，每个mark是一个，可同时设置点的 8 个方向的文字
         * 注意: Gravity 应使用 CurveView.Gravity 类
         *
         * @param position
         * @return
         */
        public Set<Mark> onCreateMarks(int position) {
            return Collections.emptySet();
        }

        /**
         * @return y 轴下限, 默认是 0
         */
        public int getMinLevel() {
            return 0;
        }

        /**
         * @return y 轴上限，默认是 100
         */
        public int getMaxLevel() {
            return 100;
        }

        /**
         * level 是 y 轴高度，在 minLevel 和 maxLevel 之间
         *
         * @param position
         * @return 返回当前 position 的 level
         */
        public abstract int getLevel(int position);

        /**
         * 获取第 i 个点 x 轴上的文字
         *
         * @param i
         * @return
         */
        public String getXAxisText(int i) {
            return "";
        }

    }

    public static class ItemDecoration {
        protected Set<Mark> mMarks = new HashSet<>();
        protected int mLevel;
        protected String mXAxisText;
    }

    public static class Mark {
        public final String content;
        public final int gravity;
        public final int marginStart;
        public final int marginEnd;
        public final int marginTop;
        public final int marginBottom;
        public final TextAppearanceSpan textAppearanceSpan;

        public Mark(String content) {
            this(content, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        }

        public Mark(String content, int gravity) {
            this(content, gravity, 0, 0, 0, 0);
        }

        public Mark(String content, int gravity, int marginStart, int marginTop, int marginEnd, int marginBottom) {
            this(content, gravity, marginStart, marginTop, marginEnd, marginBottom, null);
        }

        public Mark(String content, int gravity, int marginStart, int marginTop, int marginEnd, int marginBottom, TextAppearanceSpan mTextAppearanceSpan) {
            this.content = content;
            this.gravity = gravity;
            this.marginStart = marginStart;
            this.marginEnd = marginEnd;
            this.marginTop = marginTop;
            this.marginBottom = marginBottom;
            this.textAppearanceSpan = mTextAppearanceSpan;
        }
    }
}
