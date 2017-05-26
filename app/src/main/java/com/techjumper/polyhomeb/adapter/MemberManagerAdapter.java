package com.techjumper.polyhomeb.adapter;

import com.steve.creact.library.display.DisplayBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MemberManageBean;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class MemberManagerAdapter extends BaseRecyclerPowerfulAdapter{

    private IItemClick sItemClick;

    @Override
    public void setListener(BaseRecyclerViewHolder holder, DisplayBean bindBean, int position) {
        if ((bindBean instanceof MemberManageBean) && sItemClick != null) {
            MemberManageBean dataBean = (MemberManageBean) bindBean;

            holder.setOnItemClickListener(v -> {
                sItemClick.onMemberItemClick(dataBean);
            });
        }
    }

    public void setOnRoomItemClick(IItemClick itemClick) {
        sItemClick = itemClick;
    }

    public interface IItemClick {
        void onMemberItemClick(MemberManageBean data);
    }
}
