package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;
import android.view.ViewGroup;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.ViewPagerData;
import com.techjumper.polyhomeb.entity.ADEntity;
import com.techjumper.polyhomeb.other.ADVideoHelper;
import com.techjumper.polyhomeb.other.viewPager.NetWorkImageHolderView;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.AutoScrollViewPager;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.CBViewHolderCreator;
import com.techjumper.polyhomeb.widget.autoScrollViewPager.IItemClickListener;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "ViewPagerDataBean", data = ViewPagerData.class)
public class ViewPagerViewHolder extends BaseRecyclerViewHolder<ViewPagerData>
        implements IItemClickListener {

    public static final int LAYOUT_ID = R.layout.item_home_page_view_pager;

    public ViewPagerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ViewPagerData data) {
        resetRootViewHeight();
        if (data == null) return;

        ADEntity adInfos = data.getAdInfos();

        if (adInfos == null) return;

        List<ADEntity.DataBean.AdInfosBean> ad_infos = adInfos.getData().getAd_infos();

        AutoScrollViewPager view = getView(R.id.vp);
        NetWorkImageHolderView netWorkImageHolderView = new NetWorkImageHolderView(this);
        view.setOnItemClickListener(netWorkImageHolderView);
        view.setOnPageChangeListener(netWorkImageHolderView);
        view.setPages(new CBViewHolderCreator<NetWorkImageHolderView>() {
            @Override
            public NetWorkImageHolderView createHolder() {
                return netWorkImageHolderView;
            }
        }, ad_infos)
                .startTurning(5000)  //默认滚动时间间隔，如果服务器某个数据没有返回这个字段，那么就采用默认时间
                .setPageIndicatorAlign(AutoScrollViewPager.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageIndicator(new int[]{R.mipmap.icon_dot_grey, R.mipmap.icon_dot_green});
    }

    private void resetRootViewHeight() {
        View rootView = getView(R.id.root);
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        layoutParams.height = RuleUtils.getScreenWidth() / 2;
        rootView.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(int position, Object object, ADVideoHelper helper, View itemView) {
        ADEntity.DataBean.AdInfosBean bean = (ADEntity.DataBean.AdInfosBean) object;
        switch (bean.getMedia_type()) {
            //图片
            case 1:
            default:
                ToastUtils.show(position + "..." + bean.getUrl());
                break;
            //视频
            case 2:
                helper.onClick(itemView);
                break;
        }
    }
}
