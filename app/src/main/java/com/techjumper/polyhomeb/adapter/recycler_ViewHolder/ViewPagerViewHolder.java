package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.ViewPagerData;
import com.techjumper.polyhomeb.entity.ADEntity;
import com.techjumper.polyhomeb.other.viewPager.NetWorkImageHolderView;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.AutoScrollViewPager;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.CBViewHolderCreator;
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
        implements OnItemClickListener {

    public static final int LAYOUT_ID = R.layout.item_home_page_view_pager;

    public ViewPagerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ViewPagerData data) {
        if (data == null) return;

        ADEntity adInfos = data.getAdInfos();

        if (adInfos == null) return;

        List<ADEntity.DataBean.AdInfosBean> ad_infos = adInfos.getData().getAd_infos();

        AutoScrollViewPager view = getView(R.id.vp);
        view.setPages(new CBViewHolderCreator<NetWorkImageHolderView>() {
            @Override
            public NetWorkImageHolderView createHolder() {
                return new NetWorkImageHolderView();
            }
        }, ad_infos)
                .startTurning(4500)
                //设置指示器的方向
                //.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                //.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position, Object object) {
        ADEntity.DataBean.AdInfosBean bean = (ADEntity.DataBean.AdInfosBean) object;
        ToastUtils.show(bean.getUrl());
    }
}
