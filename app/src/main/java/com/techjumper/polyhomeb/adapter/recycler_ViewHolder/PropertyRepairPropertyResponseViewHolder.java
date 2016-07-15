package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairPropertyResponseData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyRepairPropertyResponseBean", data = PropertyRepairPropertyResponseData.class)
public class PropertyRepairPropertyResponseViewHolder extends BaseRecyclerViewHolder<PropertyRepairPropertyResponseData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_property_response;

    public PropertyRepairPropertyResponseViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyRepairPropertyResponseData data) {
        if (data == null) return;
        setText(R.id.tv_property_response, data.getResponse());
    }
}
