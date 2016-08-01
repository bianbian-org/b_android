package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyPlacardDividerLongBean", data = PropertyPlacardDividerLongData.class)
//data和bean可以用另一个divider的
public class PropertyRepairDividerLongViewHolder extends BaseRecyclerViewHolder<PropertyPlacardDividerLongData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_divider_long;

    public PropertyRepairDividerLongViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyPlacardDividerLongData data) {
        if (data == null) return;
    }
}
