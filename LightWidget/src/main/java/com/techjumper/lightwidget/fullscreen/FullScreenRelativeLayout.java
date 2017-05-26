package com.techjumper.lightwidget.fullscreen;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class FullScreenRelativeLayout extends RelativeLayout {
    public FullScreenRelativeLayout(Context context) {
        super(context);
    }

    public FullScreenRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FullScreenRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        super.onMeasure(MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY)
                , MeasureSpec.makeMeasureSpec(screenHeight, MeasureSpec.EXACTLY));
    }
}
