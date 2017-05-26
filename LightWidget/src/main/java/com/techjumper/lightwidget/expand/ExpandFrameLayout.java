package com.techjumper.lightwidget.expand;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.techjumper.lightwidget.R;
import com.techjumper.lightwidget.ratio.RatioFrameLayout;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/10
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ExpandFrameLayout extends FrameLayout {

    private static final String ANIM_FIELD_NAME = "viewHeight";
    private static int sAnimDuration = 200; //毫秒

    private ObjectAnimator mAnimator;
    private IExpandListener iExpandListener;
    private int mCollapseHeight;
    private int mExpandHeight;
    private boolean mDisableTouchWhenExpand;

    public ExpandFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public ExpandFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ExpandFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.expand);
        mDisableTouchWhenExpand = ta.getBoolean(R.styleable.expand_disableTouchWhenExpand, false);
        ta.recycle();
    }


    public void setOnExpandListener(IExpandListener listener) {
        this.iExpandListener = listener;
    }

    public void setMinAndMaxHeight(int min, int max) {
        mCollapseHeight = min;
        mExpandHeight = max;
    }

    public void toggle() {
        if (isExpand()) {
            collapse();
        } else {
            expand();
        }
    }

    public boolean isExpand() {
        return getHeight() >= mExpandHeight;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isExpand()) {
            return true;
        } else if (!mDisableTouchWhenExpand) {
            return true;
        }
        return false;
    }


    public void expand() {
        startAnim(mExpandHeight, false);
    }

    public void collapse() {
        startAnim(mCollapseHeight, true);
    }

    private void cancelAnimator() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    public void startAnim(int height, boolean isCollapse) {
        cancelAnimator();
        mAnimator = ObjectAnimator.ofInt(this, ANIM_FIELD_NAME, height)
                .setDuration(sAnimDuration);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (iExpandListener != null) {
                    if (isCollapse) {
                        iExpandListener.onCollapsed();
                    } else {
                        iExpandListener.onExpanded();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    public void setViewHeight(int height) {
        if (getLayoutParams() == null) return;
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = height;
        requestLayout();
    }

    public int getViewHeight() {
        if (getLayoutParams() == null) return 0;
        ViewGroup.LayoutParams params = getLayoutParams();
        return params.height;
    }

    public interface IExpandListener {
        void onExpanded();

        void onCollapsed();
    }
}
