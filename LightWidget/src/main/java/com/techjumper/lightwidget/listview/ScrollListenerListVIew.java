package com.techjumper.lightwidget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.techjumper.lightwidget.abstracts.IScrollChange;
import com.techjumper.lightwidget.girdview.HeaderGridView;
import com.techjumper.lightwidget.utils.ScrollUtils;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ScrollListenerListVIew extends ListView {
    private IScrollChange iScrollChange;

    private float mDownY;
    private boolean mReachBottom;
    private int mTouchSlop;


    public ScrollListenerListVIew(Context context) {
        super(context);
        init(null);
    }

    public ScrollListenerListVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ScrollListenerListVIew(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (iScrollChange == null) return;
//                if (ScrollUtils.isReachBottom(view, scrollState)) {
//                    if (!mReachBottom) {
//                        mReachBottom = true;
//                        iScrollChange.onBottom(true);
//                    }
//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (iScrollChange == null) return;
                iScrollChange.onBottom(ScrollUtils.isReachBottom(view));
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDownY == 0) {
                    mDownY = ev.getY();
                    break;
                }
                if (Math.abs(ev.getY() - mDownY) < mTouchSlop) {
                    break;
                }
                if (mReachBottom && !canScrollVertically(-1)) {
                    break;
                }
                if (mReachBottom && ev.getY() >= mDownY && iScrollChange != null) {
                    mReachBottom = false;
                    iScrollChange.onBottom(false);
                }
                mDownY = 0;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mDownY = 0;
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnScrollListener(IScrollChange iScrollChange) {
        this.iScrollChange = iScrollChange;
    }

}
