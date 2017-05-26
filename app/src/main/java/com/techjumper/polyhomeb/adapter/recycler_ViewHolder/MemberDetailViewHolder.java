package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MemberDetailData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MemberDetailData.class, beanName = "MemberDetailBean")
public class MemberDetailViewHolder extends BaseRecyclerViewHolder<MemberDetailData> {

    public static final int LAYOUT_ID = R.layout.item_member_detail;

    public MemberDetailViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MemberDetailData data) {
        if (data == null) return;
        setText(R.id.title, data.getRoomName());
    }
}
