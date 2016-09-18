package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalMainData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MedicalMainData.class, beanName = "MedicalMainBean")
public class MedicalMainViewHolder extends BaseRecyclerViewHolder<MedicalMainData> {

    public static final int LAYOUT_ID = R.layout.item_medical_main;

    public MedicalMainViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MedicalMainData data) {
        if (data == null) return;
    }
}
