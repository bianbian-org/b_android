package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyRepairBigDividerBean", data = PropertyRepairBigDividerData.class)
public class PropertyRepairBigDividerViewHolder extends BaseRecyclerViewHolder<PropertyRepairBigDividerData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_big_divider;

    public PropertyRepairBigDividerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyRepairBigDividerData data) {
        if (data == null) return;
        if (data.getColor() == 0) return;
        getView(R.id.layout).setBackgroundResource(data.getColor());
    }
}
