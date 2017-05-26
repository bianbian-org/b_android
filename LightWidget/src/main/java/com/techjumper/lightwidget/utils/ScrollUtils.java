package com.techjumper.lightwidget.utils;

import android.view.View;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ScrollUtils {
//        public static boolean isReachBottomAccuracy(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            return firstVisibleItem + visibleItemCount == totalItemCount
//                    && view.getChildAt(view.getChildCount() - 1).getBottom() <= view.getHeight();
//        }
//
//    public static boolean isReachBottom(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        return firstVisibleItem + visibleItemCount == totalItemCount;
//    }
//
//    public static boolean isReachBottom(AbsListView view, int scrollState) {
//        if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
//            if (view != null && view.getAdapter() != null && view.getChildAt(view.getChildCount() - 1) != null
//                    && view.getLastVisiblePosition() == view.getAdapter().getCount() - 1
//                    && view.getChildAt(view.getChildCount() - 1).getBottom() <= view.getHeight()) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static boolean isReachBottom(View view) {
        if (view == null) return true;

        if (android.os.Build.VERSION.SDK_INT < 14) {
            return view.getScrollY() > 0;
        } else {
            return view.canScrollVertically(-1);
        }
    }
}
