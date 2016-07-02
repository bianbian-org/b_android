package com.techjumper.polyhomeb.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AutoScrollerViewPager extends ViewPager {

    /**
     * 图片自动切换时间
     */
    private static int PHOTO_CHANGE_TIME = 2000;
    /**
     * 公吿的数目
     */
    private int length;
    /**
     * ViewPager的Scroller
     */
    private MyScroller myScroller;
    /**
     * 是否能滑动
     */
    private boolean isScrollable = true;
    /**
     * 是否轮播
     */
    private boolean isContinue = true;
    /**
     * 控制轮播的timer
     */
    private Timer timer;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (length > 1) {
                setCurrentItem(getCurrentItem() + 1);
            }
        }
    };

    public AutoScrollerViewPager(Context context) {
        this(context, null);
    }

    public AutoScrollerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScroller();
    }

    /**
     * 通过反射将ViewPager翻页的scroller时间控制
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initScroller() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            myScroller = new MyScroller(getContext());
            myScroller.setDuration(1000);
            field.set(this, myScroller);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    isContinue = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    isContinue = false;
                    break;
                case MotionEvent.ACTION_UP:
                    isContinue = true;
                    break;
                default:
                    isContinue = true;
                    break;
            }
            return super.onTouchEvent(ev);
        }
    }

    public void startTimer() {
        if (isScrollable) {
            if (timer == null) {
                timer = new Timer();
            } else {
                timer.purge();
                timer.cancel();
                timer = new Timer();
            }

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isContinue) {
                        handler.sendEmptyMessage(1);
                    }
                }
            }, PHOTO_CHANGE_TIME, PHOTO_CHANGE_TIME);
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
    }

    /**
     * 自定义Scroller,滑动切换的时间快慢可以控制
     */
    private class MyScroller extends Scroller {
        private int duration;

        public MyScroller(Context context) {
            super(context);
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            duration = this.duration;
            super.startScroll(startX, startY, dx, dy, duration);
        }
    }


    public void setLength(int length) {
        this.length = length;
    }
}
