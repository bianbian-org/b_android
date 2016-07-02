package com.techjumper.polyhomeb.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeViewPager extends ViewPager {

    private boolean isCanScroll = true;

    public HomeViewPager(Context context) {
        super(context);
    }

    public HomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isCanScroll) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 如果不重写,那么在点击切换VP的时候,用手去触摸VP,会导致VP的切换中止,界面上会显示两个Fragment,另外在这种情况下,还能手动一点一点的滑动VP
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isCanScroll) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }



}