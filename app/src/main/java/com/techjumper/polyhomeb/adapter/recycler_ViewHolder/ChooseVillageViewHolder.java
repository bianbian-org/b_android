package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.ChooseVillageData;
import com.techjumper.polyhomeb.mvp.v.activity.JoinVillageActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "ChooseVillageBean", data = ChooseVillageData.class)
public class ChooseVillageViewHolder extends BaseRecyclerViewHolder<ChooseVillageData> {

    public static final int LAYOUT_ID = R.layout.item_choose_village;

    public ChooseVillageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ChooseVillageData data) {
        if (data == null) return;
        int id = data.getId();
        setText(R.id.tv_village, data.getName());
        setOnClickListener(R.id.layout_village, v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.CHOOSE_VILLAGE_ID, data.getId());
            bundle.putString(Constant.CHOOSE_VILLAGE_NAME, data.getName());
            new AcHelper.Builder((Activity) getContext()).extra(bundle).target(JoinVillageActivity.class).start();
        });
    }
}
