package com.techjumper.lightwidget.ratio;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.techjumper.lightwidget.R;


/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RatioLinearLayout extends LinearLayout {

    private float mRatio;
    private boolean notFirst;

    public RatioLinearLayout(Context context) {
        super(context);
        init(null);
    }

    public RatioLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RatioLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatioLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected boolean init(AttributeSet attrs) {
        if (notFirst) return false;
        if (attrs == null) return false;
        notFirst = true;
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ratio);
        mRatio = ta.getFloat(R.styleable.ratio_ratio, 1.F);
        ta.recycle();
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        notFirst = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatio == -1.F) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) (width * mRatio), MeasureSpec.EXACTLY));

    }
}
