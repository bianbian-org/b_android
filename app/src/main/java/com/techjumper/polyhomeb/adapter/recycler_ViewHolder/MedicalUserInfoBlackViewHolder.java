package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalUserInfoBlackData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MedicalUserInfoBlackData.class, beanName = "MedicalUserInfoBlackBean")
public class MedicalUserInfoBlackViewHolder extends BaseRecyclerViewHolder<MedicalUserInfoBlackData> {

    public static final int LAYOUT_ID = R.layout.item_medical_user_info_black;

    public MedicalUserInfoBlackViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MedicalUserInfoBlackData data) {
        if (data == null) return;
    }
}
