package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.RepairDetailTimeData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "RepairDetailTimeBean", data = RepairDetailTimeData.class)
public class PropertyRepairDetailTimeViewHolder extends BaseRecyclerViewHolder<RepairDetailTimeData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_detail_time;

    public PropertyRepairDetailTimeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(RepairDetailTimeData data) {
        if (data == null) return;
        setText(R.id.tv_time, data.getTime());
    }
}
