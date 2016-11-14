package com.techjumper.polyhome.b.home.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.techjumper.polyhome.b.home.widget.loop_pager.LoopViewPager;

/**
 * Created by kevin on 16/4/25.
 */
public class AdViewPager extends LoopViewPager {
    public AdViewPager(Context context) {
        super(context);
    }

    public AdViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    public static final int RESUME = 0;
//    public static final int PAUSE = 1;
//    public static final int DESTROY = 2;
//    private PageChangeListener mPageChangeListener;
//    private OnPageChangeListener iClientPageChangeListener;
//
//    @IntDef({RESUME, PAUSE, DESTROY})
//    @Retention(RetentionPolicy.SOURCE)
//    public @interface LifeCycle {
//
//    }
//
//    private int lifeCycle = RESUME;
//    private boolean isTouching = false;
//
//    public AdViewPager(Context context) {
//        super(context);
//    }
//
//    public AdViewPager(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public void setLifeCycle(@LifeCycle int lifeCycle) {
//        this.lifeCycle = lifeCycle;
//    }
//
//    @Override
//    public void setAdapter(PagerAdapter adapter) {
//        super.setAdapter(adapter);
//        if (!(adapter instanceof AdViewPagerAdapter))
//            return;
//        AdViewPagerAdapter adPagerAdapter = (AdViewPagerAdapter) adapter;
//        int num = initalPosition(adPagerAdapter);
//        setCurrentItem(num, false);
//    }
//
//    private int initalPosition(AdViewPagerAdapter adPagerAdapter) {
//        if (adPagerAdapter == null || adPagerAdapter.getViews() == null || adPagerAdapter.getViews().size() == 0)
//            return 0;
//        return AdViewPagerAdapter.MAX_SIZE / 2 - AdViewPagerAdapter.MAX_SIZE / 2 % adPagerAdapter.getViews().size();
//    }
//
//    @Override
//    public void setCurrentItem(int item) {
//        int processItem = processSetCurrentItem(item, false);
//        item = processItem == -1 ? item : processItem;
//        super.setCurrentItem(item);
//    }
//
//    @Override
//    public void setCurrentItem(int item, boolean smoothScroll) {
//        int processItem = processSetCurrentItem(item, smoothScroll);
//        item = processItem == -1 ? item : processItem;
//        super.setCurrentItem(item, smoothScroll);
//    }
//
//    private int processSetCurrentItem(int item, boolean smoothScroll) {
//        PagerAdapter adapter = getAdapter();
//        if (adapter == null) {
//            return -1;
//        } else if (!(adapter instanceof AdViewPagerAdapter)) {
//            super.setCurrentItem(item, smoothScroll);
//            return -1;
//        }
//        AdViewPagerAdapter adPagerAdapter = (AdViewPagerAdapter) adapter;
//        if (adPagerAdapter.getViews() == null) {
//            return -1;
//        }
//        if (item < adPagerAdapter.getViews().size()) {
//            item = initalPosition(adPagerAdapter) + item;
//        }
//        return item;
//    }
//
//    @Override
//    public void addOnPageChangeListener(OnPageChangeListener listener) {
//        if (mPageChangeListener == null) {
//            mPageChangeListener = new PageChangeListener();
//            super.addOnPageChangeListener(mPageChangeListener);
//        }
//        iClientPageChangeListener = listener;
//    }
//
//    @Override
//    public void removeOnPageChangeListener(OnPageChangeListener listener) {
//        if (mPageChangeListener != null) {
//            super.removeOnPageChangeListener(mPageChangeListener);
//            mPageChangeListener = null;
//        }
//        iClientPageChangeListener = null;
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
////        switch (ev.getAction()) {
////            case MotionEvent.ACTION_DOWN:
////            case MotionEvent.ACTION_MOVE:
////                isTouching = true;
////                break;
////            case MotionEvent.ACTION_CANCEL:
////            case MotionEvent.ACTION_UP:
////                isTouching = false;
////                break;
////        }
//        return super.onTouchEvent(ev);
//    }
//
//    private class PageChangeListener implements ViewPager.OnPageChangeListener {
//
//        private boolean isValid() {
//            return getAdAdapter() != null && getAdAdapter().getViews() != null && getAdAdapter().getViews().size() != 0;
//        }
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            if (iClientPageChangeListener == null)
//                return;
//            if (!isValid()) {
//                iClientPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                return;
//            }
//            iClientPageChangeListener.onPageScrolled(position % getAdAdapter().getViews().size(), positionOffset, positionOffsetPixels);
//        }
//
//
//        @Override
//        public void onPageSelected(int position) {
//            if (iClientPageChangeListener == null)
//                return;
//            if (!isValid()) {
//                iClientPageChangeListener.onPageSelected(position);
//                return;
//            }
//            iClientPageChangeListener.onPageSelected(position % getAdAdapter().getViews().size());
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//            if (iClientPageChangeListener == null)
//                return;
//            iClientPageChangeListener.onPageScrollStateChanged(state);
//        }
//
//        private AdViewPagerAdapter getAdAdapter() {
//            if (getAdapter() == null || !(getAdapter() instanceof AdViewPagerAdapter))
//                return null;
//            return (AdViewPagerAdapter) getAdapter();
//        }
//    }
}
