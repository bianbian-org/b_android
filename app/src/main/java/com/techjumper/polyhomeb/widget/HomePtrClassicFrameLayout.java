package com.techjumper.polyhomeb.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.techjumper.ptr_lib.PtrClassicFrameLayout;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomePtrClassicFrameLayout extends PtrClassicFrameLayout {

    private float startY;
    private float startX;
    // 记录viewPager是否拖拽的标记
    private boolean mIsDraggerByOrSlide2UnlockView;
    private final int mTouchSlop;

    public HomePtrClassicFrameLayout(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public HomePtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public HomePtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsDraggerByOrSlide2UnlockView = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager或者滑动解锁控件正在拖拽中，那么不拦截它的事件，直接return false；
                if(mIsDraggerByOrSlide2UnlockView) {
                    return false;
                }
                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager或者滑动解锁控件处理。
                if(distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsDraggerByOrSlide2UnlockView = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsDraggerByOrSlide2UnlockView = false;
                break;
        }
        // 如果是Y轴位移大于X轴，事件交给PtrClassicFrameLayout处理。
        return super.onInterceptTouchEvent(ev);
    }
}
