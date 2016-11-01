package com.techjumper.polyhomeb.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NonScrollViewPager extends ViewPager {

    private boolean canScroll;

    public NonScrollViewPager(Context context) {
        super(context);
    }

    public NonScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!canScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!canScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
}
