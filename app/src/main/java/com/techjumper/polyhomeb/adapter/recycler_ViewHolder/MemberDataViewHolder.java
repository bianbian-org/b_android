package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MemberManageData;
import com.techjumper.polyhomeb.entity.C_RoomsByMemberEntity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MemberManageData.class, beanName = "MemberManageBean")
public class MemberDataViewHolder extends BaseRecyclerViewHolder<MemberManageData> {

    public static final int LAYOUT_ID = R.layout.item_member_manage;

    public MemberDataViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MemberManageData data) {
        if (data == null) return;
        StringBuilder stringBuilder = new StringBuilder();
        C_RoomsByMemberEntity entity = data.getEntity();
        if (entity != null && entity.getData() != null && entity.getData().getResult() != null
                && entity.getData().getResult().size() != 0) {
            List<C_RoomsByMemberEntity.DataEntity.ResultEntity> result = entity.getData().getResult();
            for (int i = 0; i < result.size(); i++) {
                C_RoomsByMemberEntity.DataEntity.ResultEntity resultEntity = result.get(i);
                String room_name = resultEntity.getRoom_name();
                stringBuilder.append(room_name + " ");
            }
        }
        setText(R.id.title, data.getMemberName());
        if (data.isDeleteMode()) {
            setImageDrawable(R.id.iv_arrow, ResourceUtils.getDrawableRes(R.mipmap.icon_delete_red));
            getView(R.id.tv_rooms).setVisibility(View.GONE);
        } else {
            setImageDrawable(R.id.iv_arrow, ResourceUtils.getDrawableRes(R.mipmap.icon_right_arrow));
            getView(R.id.tv_rooms).setVisibility(View.VISIBLE);
            setText(R.id.tv_rooms, stringBuilder.toString());
        }
    }
}
