package com.techjumper.polyhomeb.widget.autoScrollViewPager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.ADEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/4
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AutoScrollViewPager extends LinearLayout {
    private List<ADEntity.DataBean.AdInfosBean> mDatas;
    private int[] page_indicatorId;
    private ArrayList<ImageView> mPointViews = new ArrayList<ImageView>();
    private CBPageChangeListener pageChangeListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private CBPageAdapter pageAdapter;
    private CBLoopViewPager viewPager;
    private ViewPagerScroller scroller;
    private ViewGroup loPageTurningPoint;
    private long autoTurningTime;   //默认滚动时间
    private boolean turning;
    private boolean canTurn = false;
    private boolean manualPageable = true;
    private boolean canLoop = true;

    private Subscription mSubs1;

    public enum PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }

    public AutoScrollViewPager(Context context) {
        super(context);
        init(context);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoScrollViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View hView = LayoutInflater.from(context).inflate(
                R.layout.include_viewpager, this, true);
        viewPager = (CBLoopViewPager) hView.findViewById(R.id.cbLoopViewPager);
        loPageTurningPoint = (ViewGroup) hView
                .findViewById(R.id.loPageTurningPoint);
        initViewPagerScroll();

        startScroll();
    }

    private int mPosition = 0;  //数据源当前的下标
    private int mCurrentADPlayTime = 0;  //当前广告播放时长

    /**
     * 开始滚动，不同图片或者视频根据服务器返回的数据，展示不同的时间
     */
    private void startScroll() {
        //重置(下拉刷新之后重置为0)
        mPosition = 0;
        mCurrentADPlayTime = 0;
        RxUtils.unsubscribeIfNotNull(mSubs1);
        mSubs1 = Observable.interval(0, 1, TimeUnit.SECONDS)
                .filter(aLong -> {
                    List<ADEntity.DataBean.AdInfosBean> mDatas = AutoScrollViewPager.this.mDatas;
                    ADEntity.DataBean.AdInfosBean bean = mDatas.get(mPosition >= AutoScrollViewPager.this.mDatas.size()
                            ? mPosition % AutoScrollViewPager.this.mDatas.size() : mPosition);
                    String time = TextUtils.isEmpty(bean.getRunning_time()) ? String.valueOf(autoTurningTime) : bean.getRunning_time();
                    int playTime = NumberUtil.convertToint(time, (int) autoTurningTime);
                    return mCurrentADPlayTime++ == playTime;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (viewPager == null || !turning) return;
                        mPosition++;
                        mCurrentADPlayTime = 0;
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                });
    }

    public AutoScrollViewPager setPages(int realSize, CBPageAdapter adapter) {
        pageAdapter = adapter;
        this.mDatas = adapter.getDatas();
        viewPager.setAdapter(pageAdapter, canLoop);
        if (page_indicatorId != null)
            setPageIndicator(realSize, page_indicatorId);
        return this;
    }

    /**
     * 通知数据变化
     * 如果只是增加数据建议使用 notifyDataSetAdd()
     */
    public void notifyDataSetChanged(int realSize) {
        viewPager.getAdapter().notifyDataSetChanged();
        if (page_indicatorId != null)
            setPageIndicator(realSize, page_indicatorId);
    }

    /**
     * 设置底部指示器是否可见
     *
     * @param visible
     */
    public AutoScrollViewPager setPointViewVisible(boolean visible) {
        loPageTurningPoint.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 底部指示器资源图片
     *
     * @param realSize
     * @param page_indicatorId
     */
    public AutoScrollViewPager setPageIndicator(int realSize, int[] page_indicatorId) {
        loPageTurningPoint.removeAllViews();
        mPointViews.clear();
        this.page_indicatorId = page_indicatorId;
        if (mDatas == null) return this;
//        for (int count = 0; count < mDatas.size(); count++) {
        for (int count = 0; count < realSize; count++) {
            // 翻页指示的点
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(5, 0, 5, 0);
            if (mPointViews.isEmpty())
                pointView.setImageResource(page_indicatorId[1]);
            else
                pointView.setImageResource(page_indicatorId[0]);
            mPointViews.add(pointView);
            loPageTurningPoint.addView(pointView);
        }
        pageChangeListener = new CBPageChangeListener(realSize, mPointViews, page_indicatorId);
        viewPager.setOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(viewPager.getRealItem());
        if (onPageChangeListener != null)
            pageChangeListener.setOnPageChangeListener(onPageChangeListener);

        return this;
    }

    /**
     * 指示器的方向
     *
     * @param align 三个方向：居左 （RelativeLayout.ALIGN_PARENT_LEFT），居中 （RelativeLayout.CENTER_HORIZONTAL），居右 （RelativeLayout.ALIGN_PARENT_RIGHT）
     * @return
     */
    public AutoScrollViewPager setPageIndicatorAlign(PageIndicatorAlign align) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) loPageTurningPoint.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, align == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, align == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, align == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        loPageTurningPoint.setLayoutParams(layoutParams);
        return this;
    }

    /***
     * 是否开启了翻页
     *
     * @return
     */
    public boolean isTurning() {
        return turning;
    }

    /***
     * 开始翻页
     *
     * @param autoTurningTime 自动翻页时间（默认自动翻页时间）
     * @return
     */
    public AutoScrollViewPager startTurning(long autoTurningTime) {
        if (mDatas == null || mDatas.size() == 0) return this;
        //如果是正在翻页的话先停掉
        if (turning) {
            stopTurning();
        }
        //设置可以翻页并开启翻页
        canTurn = true;
        this.autoTurningTime = autoTurningTime;
        turning = true;
        startScroll();
        return this;
    }

    public void stopTurning() {
        turning = false;
        RxUtils.unsubscribeIfNotNull(mSubs1);
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(
                    viewPager.getContext());
            mScroller.set(viewPager, scroller);

        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void setManualPageable(boolean manualPageable) {
        viewPager.setCanScroll(manualPageable);
    }

    //触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            // 开始翻页
            if (canTurn) startTurning(autoTurningTime);
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 停止翻页
            if (canTurn) stopTurning();
        }
        return super.dispatchTouchEvent(ev);
    }

    //获取当前的页面index
    public int getCurrentItem() {
        if (viewPager != null) {
            return viewPager.getRealItem();
        }
        return -1;
    }

    //设置当前的页面index
    public void setCurrentItem(int index) {
        if (viewPager != null) {
            viewPager.setCurrentItem(index);
        }
    }

    /**
     * 设置翻页监听器
     */
    public AutoScrollViewPager setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        //如果有默认的监听器（即是使用了默认的翻页指示器）则把用户设置的依附到默认的上面，否则就直接设置
        if (pageChangeListener != null)
            pageChangeListener.setOnPageChangeListener(onPageChangeListener);
        else viewPager.setOnPageChangeListener(onPageChangeListener);
        return this;
    }

    /**
     * 监听item点击
     */
    public AutoScrollViewPager setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener == null) {
            viewPager.setOnItemClickListener(null);
            return this;
        }
        viewPager.setOnItemClickListener(onItemClickListener);
        return this;
    }

    /**
     * 设置ViewPager的滚动速度
     */
    public void setScrollDuration(int scrollDuration) {
        scroller.setScrollDuration(scrollDuration);
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        viewPager.setCanLoop(canLoop);
    }

    public void setOffscreenPageLimit(int limit) {
        viewPager.setOffscreenPageLimit(limit);
    }
}
