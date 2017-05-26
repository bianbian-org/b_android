package com.techjumper.polyhomeb.adapter;

import com.steve.creact.library.display.DisplayBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRoomBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.RoomManageBean;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class RoomManageAdapter extends BaseRecyclerPowerfulAdapter {

    private IItemClick sItemClick;

    @Override
    public void setListener(BaseRecyclerViewHolder holder, DisplayBean bindBean, int position) {
        if ((bindBean instanceof RoomManageBean) && sItemClick != null) {
            RoomManageBean dataBean = (RoomManageBean) bindBean;

            holder.setOnItemClickListener(v -> {
                sItemClick.onRoomItemClick(dataBean);
            });
        }

        if ((bindBean instanceof NewRoomBean) && sItemClick != null) {
            NewRoomBean dataBean = (NewRoomBean) bindBean;

            holder.setOnItemClickListener(v -> {
                sItemClick.onNewRoomItemClick(dataBean);
            });
        }
    }

    public void setOnRoomItemClick(IItemClick itemClick) {
        sItemClick = itemClick;
    }

    public interface IItemClick {
        void onRoomItemClick(RoomManageBean data);

        void onNewRoomItemClick(NewRoomBean data);
    }
}
