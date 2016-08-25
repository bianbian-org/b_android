package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.ChooseVillageData;

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
        setOnClickListener(R.id.layout_village, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("点击了" + data.getName() + "...id:" + data.getId());
            }
        });
    }
}
