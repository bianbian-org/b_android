package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.ViewPagerData;
import com.techjumper.polyhomeb.entity.ADEntity;
import com.techjumper.polyhomeb.entity.event.LifeCycleEvent;
import com.techjumper.polyhomeb.entity.event.RVScrollEvent;
import com.techjumper.polyhomeb.widget.ADVideoLayout;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.AutoScrollViewPager;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.CBPageAdapter;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "ViewPagerDataBean", data = ViewPagerData.class)
public class ViewPagerViewHolder extends BaseRecyclerViewHolder<ViewPagerData>
        implements OnItemClickListener, ViewPager.OnPageChangeListener {

    public static final int LAYOUT_ID = R.layout.item_home_page_view_pager;
    private List<ADEntity.DataBean.AdInfosBean> ad_infos = new ArrayList<>();
    private AutoScrollViewPager mViewPager;
    private CBPageAdapter mAdapter;
    private int mCurrentPosition = 0;
    private int[] ints = {R.mipmap.icon_dot_grey, R.mipmap.icon_dot_green};

    public ViewPagerViewHolder(View itemView) {
        super(itemView);
        RxBus.INSTANCE.asObservable().subscribe(o -> {
            //和Fragment的生命周期绑定
            //首页RV滑动，广告item即将不可见时候停止播放
            if (o instanceof LifeCycleEvent || o instanceof RVScrollEvent) {
                stop(mCurrentPosition);
            }
        });
    }

    @Override
    public void setData(ViewPagerData data) {
        resetRootViewHeight();
        if (data == null || data.getAdInfos() == null || data.getAdInfos().getData() == null
                || data.getAdInfos().getData().getAd_infos().isEmpty())
            return;
        ad_infos.clear();
        ad_infos.addAll(data.getAdInfos().getData().getAd_infos());
        mViewPager = getView(R.id.vp);
        mAdapter = new CBPageAdapter(getContext(), ad_infos);
        switch (ad_infos.size()) {
            case 1:
                ad_infos.add(ad_infos.get(0));
                ad_infos.add(ad_infos.get(0));
                mViewPager.setPages(1, mAdapter);
                mViewPager.setPageIndicator(1, ints);
                break;
            case 2:
                ad_infos.add(ad_infos.get(0));
                ad_infos.add(ad_infos.get(1));
                mViewPager.setPages(2, mAdapter);
                mViewPager.setPageIndicator(2, ints);
                break;
            default:
                mViewPager.setPages(ad_infos.size(), mAdapter);
                mViewPager.setPageIndicator(ad_infos.size(), ints);
                break;
        }
        mViewPager.startTurning(5000)  //默认滚动时间间隔，如果服务器某个数据没有返回这个字段，那么就采用默认时间
                .setPageIndicatorAlign(AutoScrollViewPager.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setOnItemClickListener(this)
                .setOnPageChangeListener(this);
    }

    private void resetRootViewHeight() {
        View rootView = getView(R.id.root);
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        layoutParams.height = RuleUtils.getScreenWidth() / 2;
        rootView.setLayoutParams(layoutParams);
    }

    private void stop(int position) {
        if (mViewPager == null || mAdapter == null) return;
        if (ad_infos == null) return;
        if (position == -1) return;
        ADEntity.DataBean.AdInfosBean bean = ad_infos.get(mAdapter.toRealPosition(position));
        switch (bean.getMedia_type()) {
            case 2:
                View view = mViewPager.findViewWithTag(mAdapter.toRealPosition(position));
                if (view == null) break;
                ((ADVideoLayout) view).stopWork();
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        ADEntity.DataBean.AdInfosBean bean = ad_infos.get(mAdapter.toRealPosition(position));
        switch (bean.getMedia_type()) {
            case 1:
                ToastUtils.show(mAdapter.toRealPosition(position) + "..图片.." + bean.getUrl());
                break;
            case 2:
                View view = mViewPager.findViewWithTag(mAdapter.toRealPosition(position));
                if (view == null) break;
                if (((ADVideoLayout) view).isPlaying()) {
                    ToastUtils.show(mAdapter.toRealPosition(position) + "..视频.." + bean.getUrl());
                } else {
                    ((ADVideoLayout) view).onClick();
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset >= 0.7) {
            stop(position);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        stop(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
