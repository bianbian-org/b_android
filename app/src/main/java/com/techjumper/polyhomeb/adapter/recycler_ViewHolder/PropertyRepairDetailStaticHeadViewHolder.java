package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailStaticData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyRepairDetailStaticBean", data = PropertyRepairDetailStaticData.class)
public class PropertyRepairDetailStaticHeadViewHolder extends BaseRecyclerViewHolder<PropertyRepairDetailStaticData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_detail_static_head;

    public PropertyRepairDetailStaticHeadViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyRepairDetailStaticData data) {
        if (data == null) return;
        setText(R.id.tv_title, data.getTitle());
        setText(R.id.tv_content, data.getContent());
        setText(R.id.tv_time, data.getTime());
    }
}
