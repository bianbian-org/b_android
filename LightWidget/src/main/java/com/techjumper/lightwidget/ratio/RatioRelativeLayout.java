package com.techjumper.lightwidget.ratio;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.techjumper.lightwidget.R;


/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RatioRelativeLayout extends RelativeLayout {

    private static final float DEFAULT_RATIO = 1.F;

    private float mRatio = DEFAULT_RATIO;
    private boolean notFirst;

    public RatioRelativeLayout(Context context) {
        super(context);
        init(null);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatioRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected boolean init(AttributeSet attrs) {
        if (notFirst) return false;
        notFirst = true;
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ratio);
            mRatio = ta.getFloat(R.styleable.ratio_ratio, DEFAULT_RATIO);
            ta.recycle();
        }
        return true;
    }

    protected float defaultRatio() {
        return DEFAULT_RATIO;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        notFirst = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mRatio == DEFAULT_RATIO) {
            mRatio = defaultRatio();
        }

        if (mRatio == -1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) (width * mRatio), MeasureSpec.EXACTLY));

    }
}
