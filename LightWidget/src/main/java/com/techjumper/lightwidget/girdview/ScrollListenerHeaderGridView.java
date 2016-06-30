package com.techjumper.lightwidget.girdview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;

import com.techjumper.lightwidget.abstracts.IScrollChange;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ScrollListenerHeaderGridView extends HeaderGridView {
    private IScrollChange iScrollChange;

    private float mDownY;
    //    private boolean mReachBottom;
    private int mTouchSlop;
//    private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;


    public ScrollListenerHeaderGridView(Context context) {
        super(context);
        init(null);
    }

    public ScrollListenerHeaderGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ScrollListenerHeaderGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        setOnScrollListener(new OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {
//                                    if (getAdapter() == null) return;
//                                    if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//                                        // ListPos记录当前可见的List顶端的一行的位置
//                                        mLastScrollPos = view.getFirstVisiblePosition();
//                                    }
//
//                                    View v = view.getChildAt(0);
//                                    mLastScrollTop = (v == null) ? 0 : v.getTop();
                                }


                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                                     int totalItemCount) {
//                if (ScrollUtils.isReachBottom(view)) {
//                    if (!mReachBottom) {
//                        mReachBottom = true;
//                        if (iScrollChange != null)
//                            iScrollChange.onBottom(true);
//                    }
//                }
                                    if (getHeight() <= 0 || getChildCount() <= 0) {
                                        if (iScrollChange != null)
                                            iScrollChange.onBottom(true);
                                    }
                                    if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                                        View lastVisibleItemView = getChildAt(getChildCount() - 1);
                                        if (lastVisibleItemView != null && lastVisibleItemView.getBottom() + getPaddingBottom() <= getHeight()) {
                                            if (iScrollChange != null)
                                                iScrollChange.onBottom(true);
                                            return;
                                        }
                                    }
                                    if (iScrollChange != null)
                                        iScrollChange.onBottom(false);
                                }
                            }

        );
    }

//    @Override
//    public void setAdapter(ListAdapter adapter) {
//        super.setAdapter(adapter);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            postDelayed(() -> setSelectionFromTop(mLastScrollPos, mLastScrollTop)
//                    , 30);
//        }
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (mDownY == 0) {
//                    mDownY = ev.getY();
//                    break;
//                }
//                if (Math.abs(ev.getY() - mDownY) < mTouchSlop) {
//                    break;
//                }
//                if (mReachBottom && !canScrollVertically(-1)) {
//                    break;
//                }
//                if (mReachBottom && ev.getY() >= mDownY && iScrollChange != null) {
//                    mReachBottom = false;
//                    iScrollChange.onBottom(false);
//                }
//                mDownY = 0;
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                mDownY = 0;
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }

    public void setOnScrollListener(IScrollChange iScrollChange) {
        this.iScrollChange = iScrollChange;
    }

}
