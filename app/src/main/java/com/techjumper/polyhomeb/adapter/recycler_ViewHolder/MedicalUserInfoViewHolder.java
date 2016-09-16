package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalUserInfoData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MedicalUserInfoData.class, beanName = "MedicalUserInfoBean")
public class MedicalUserInfoViewHolder extends BaseRecyclerViewHolder<MedicalUserInfoData> {

    public static final int LAYOUT_ID = R.layout.item_medical_user_info;

    public MedicalUserInfoViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MedicalUserInfoData data) {
        if (data == null) return;

        setText(R.id.tv_label, data.getLabel());
        setText(R.id.tv_content, data.getContent());

        setOnClickListener(R.id.layout_root, v -> {
            
        });

    }
}
