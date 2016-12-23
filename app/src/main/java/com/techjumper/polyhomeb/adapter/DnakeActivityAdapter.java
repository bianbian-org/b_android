package com.techjumper.polyhomeb.adapter;

import com.steve.creact.library.display.DisplayBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.DnakeLockData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.DnakeLockBean;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class DnakeActivityAdapter extends BaseRecyclerPowerfulAdapter {

    private IItemClick sItemClick;

    @Override
    public void setListener(BaseRecyclerViewHolder holder, DisplayBean bindBean, int position) {
        if (!(bindBean instanceof DnakeLockBean) || sItemClick == null)
            return;
        DnakeLockBean dataBean = (DnakeLockBean) bindBean;

        holder.getView(R.id.tv_unlock).setOnClickListener(v -> {
            sItemClick.onItemClick(dataBean.getData());
        });
    }

    public void setOnItemClick(IItemClick itemClick) {
        sItemClick = itemClick;
    }

    public interface IItemClick {
        void onItemClick(DnakeLockData data);
    }
}
