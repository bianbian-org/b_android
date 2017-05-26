package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.RoomManageData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = RoomManageData.class, beanName = "RoomManageBean")
public class RoomManageViewHolder extends BaseRecyclerViewHolder<RoomManageData> {

    public static final int LAYOUT_ID = R.layout.item_room_manage;

    public RoomManageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(RoomManageData data) {
        if (data == null) return;
        setText(R.id.title, data.getRoom_name());
        if (data.isDeleteMode()) {
            setImageDrawable(R.id.iv_arrow, ResourceUtils.getDrawableRes(R.mipmap.icon_delete_red));
        } else {
            setImageDrawable(R.id.iv_arrow, ResourceUtils.getDrawableRes(R.mipmap.icon_right_arrow));
        }
    }
}
