package com.techjumper.polyhomeb.adapter;

import com.steve.creact.library.display.DisplayBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.adapter.recycler_Data.HomeMenuItemData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.HomeMenuItemBean;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeMenuAdapter extends BaseRecyclerPowerfulAdapter {

    private IItemClick sItemClick;

    @Override
    public void setListener(BaseRecyclerViewHolder holder, DisplayBean bindBean, int position) {
        if (!(bindBean instanceof HomeMenuItemBean) || sItemClick == null)
            return;
        HomeMenuItemBean dataBean = (HomeMenuItemBean) bindBean;

        holder.setOnItemClickListener(v -> {
            sItemClick.onItemClick(dataBean.getData());
        });
    }

    public void setOnItemClick(IItemClick itemClick) {
        sItemClick = itemClick;
    }

    public interface IItemClick {
        void onItemClick(HomeMenuItemData data);
    }
}
