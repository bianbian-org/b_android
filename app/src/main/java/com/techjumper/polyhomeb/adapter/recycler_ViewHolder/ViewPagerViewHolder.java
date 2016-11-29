package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.widget.ADVideoLayout;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.ViewPagerData;
import com.techjumper.polyhomeb.entity.ADEntity;
import com.techjumper.polyhomeb.entity.event.LifeCycleEvent;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.AutoScrollViewPager;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.CBPageAdapter;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.OnItemClickListener;

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
    private List<ADEntity.DataBean.AdInfosBean> ad_infos;
    private AutoScrollViewPager mViewPager;
    private CBPageAdapter mAdapter;
    private int mCurrentPosition = -1;

    public ViewPagerViewHolder(View itemView) {
        super(itemView);
        //和Fragment的声明周期绑定
        RxBus.INSTANCE.asObservable().subscribe(o -> {
            if (o instanceof LifeCycleEvent) {
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
        ad_infos = data.getAdInfos().getData().getAd_infos();
        mViewPager = getView(R.id.vp);
        mAdapter = new CBPageAdapter(getContext(), ad_infos);
        mViewPager.setPages(mAdapter)
                .startTurning(5000)  //默认滚动时间间隔，如果服务器某个数据没有返回这个字段，那么就采用默认时间
                .setPageIndicatorAlign(AutoScrollViewPager.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageIndicator(new int[]{R.mipmap.icon_dot_grey, R.mipmap.icon_dot_green})
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
                ((ADVideoLayout) view).stopWork();
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        ADEntity.DataBean.AdInfosBean bean = ad_infos.get(mAdapter.toRealPosition(position));
        switch (bean.getMedia_type()) {
            case 1:
                ToastUtils.show(mAdapter.toRealPosition(position) + "..." + bean.getUrl());
                break;
            case 2:
                View view = mViewPager.findViewWithTag(mAdapter.toRealPosition(position));
                ((ADVideoLayout) view).onClick();
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
//        switch (ad_infos.get(mAdapter.toRealPosition(position)).getMedia_type()) {
//            case 2:
//                if (mCurrent !=null) {
//                    ((ADVideoLayout)mCurrent).stopWork();
//                    mCurrent = null;
//                }
//                mCurrent = mViewPager.findViewWithTag(mAdapter.toRealPosition(position));
//                break;
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
