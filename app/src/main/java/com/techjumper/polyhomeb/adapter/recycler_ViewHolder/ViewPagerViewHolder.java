package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.AutoScrollerAdapter;
import com.techjumper.polyhomeb.adapter.recycler_Data.ViewPagerData;
import com.techjumper.polyhomeb.widget.AutoScrollerViewPager;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "ViewPagerDataBean", data = ViewPagerData.class)
public class ViewPagerViewHolder extends BaseRecyclerViewHolder<ViewPagerData> {

    public static final int LAYOUT_ID = R.layout.item_home_page_view_pager;

    public ViewPagerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ViewPagerData data) {
        if (data == null) return;

        List<Integer> mImageList = data.getDrawables();

        AutoScrollerViewPager viewPager = getView(R.id.vp);
        AutoScrollerAdapter autoScrollerAdapter = new AutoScrollerAdapter(getContext(), mImageList, v -> {
            int currentItem = viewPager.getCurrentItem() % mImageList.size();
//            ToastUtils.show("这是第" + (currentItem + 1) + "张");
        });
        int length = autoScrollerAdapter.getLength();
        viewPager.setLength(length);
        viewPager.setAdapter(autoScrollerAdapter);
        viewPager.setCurrentItem(mImageList.size() * 5, false);
        viewPager.startTimer();
    }

}
