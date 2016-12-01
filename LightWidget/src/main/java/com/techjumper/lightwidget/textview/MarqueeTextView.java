package com.techjumper.lightwidget.textview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/11/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MarqueeTextView extends TextView implements Runnable {

    private int mMarqueeRepeatLimit = 3;
    private int currentScrollX = 0; // 当前滚动位置  X轴
    private int firstScrollX = 0;  //  初始位置
    private boolean isStop = false;  // 开始停止的标记
    private int textWidth;  // 文本宽度
    private int mWidth = 0; // 控件宽度
    private int speed = 2;  // 默认是两个点
    private int delayed = 1000; // 默认是1秒
    private int endX; // 滚动到哪个位置
    private boolean isFirstDraw = true; // 当首次或文本改变时重置
    private static final int SCROLL_DELAYED = 4 * 1000;

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isSelected() {
        return true;
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    /**
     * 滚动速度
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * 滚动时间间隔
     */
    public void setDelayed(int delayed) {
        this.delayed = delayed;
    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);  // 清空队列
        postDelayed(this, SCROLL_DELAYED);  // 4秒之后滚动到指定位置
    }

    /**
     * 停止滑动
     */
    public void stopScroll() {
        isStop = true;
    }

    /**
     * 从头开始滑动
     */
    public void startFor() {
        currentScrollX = 0;  // 将当前位置置为0
        startScroll();
    }

    public void setMarqueeRepeatLimit(int marqueeLimit) {
        mMarqueeRepeatLimit = marqueeLimit;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        isStop = true; // 停止滚动
        this.removeCallbacks(this);   // 清空队列
        currentScrollX = firstScrollX;  // 滚动到初始位置
        this.scrollTo(currentScrollX, 0);
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        // 需要重新设置参数
        isFirstDraw = true;
        isStop = false;
        postDelayed(this, SCROLL_DELAYED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirstDraw) {
            getTextWidth_();
            firstScrollX = getScrollX(); // 获取第一次滑动的X轴距离
            currentScrollX = firstScrollX;
            mWidth = this.getWidth();  // 获取文本宽度，如果文本宽度大于屏幕宽度，则为屏幕宽度，否则为文本宽度
            endX = firstScrollX + textWidth - mWidth / 2;  // 滚动的最大距离，可根据需要来定
            isFirstDraw = false;
        }
    }

    /**
     * 执行滚动
     */
    @Override
    public void run() {
        currentScrollX += speed;  // 滚动速度每次加几个点
        scrollTo(currentScrollX, 0); // 滚动到指定位置
        if (isStop) return;
        if (currentScrollX >= endX) {   // 如果滚动的位置大于最大限度则滚动到初始位置
            scrollTo(firstScrollX, 0);
            currentScrollX = firstScrollX; // 初始化滚动速度
            postDelayed(this, SCROLL_DELAYED);  // SCROLL_DELAYED毫秒之后重新滚动
        } else {
            postDelayed(this, delayed);  // delayed毫秒之后再滚动到指定位置
        }
    }

    private void getTextWidth_() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);
    }

    public int getTextWidth() {
        return textWidth;
    }
}
