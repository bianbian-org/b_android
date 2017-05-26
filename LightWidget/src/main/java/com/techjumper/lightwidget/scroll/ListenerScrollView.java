package com.techjumper.lightwidget.scroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.techjumper.lightwidget.abstracts.IScrollChange;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ListenerScrollView extends ScrollView {

    private IScrollChange iScrollChange;

    private View mContentView;

    public ListenerScrollView(Context context) {
        super(context);
    }

    public ListenerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListenerScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (iScrollChange == null) return;
        iScrollChange.onScrollChange(l, t, oldl, oldt);

        if (isBottom()) {
            iScrollChange.onBottom(true);
        } else if (getScrollY() == 0) {
            iScrollChange.onTop(true);
        } else {
            iScrollChange.onBottom(false);
            iScrollChange.onTop(false);
        }

    }

    public boolean isBottom() {
//        return mContentView != null && mContentView.getMeasuredHeight() <= getScrollY() + getHeight();
        return !canScrollVertically(1);
    }

    public void setOnScrollListener(IScrollChange iScrollChange) {
        this.iScrollChange = iScrollChange;

        mContentView = getChildAt(0);
    }


}
