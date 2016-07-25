package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailPropertyContentData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyRepairDetailPropertyBean", data = PropertyRepairDetailPropertyContentData.class)
public class PropertyRepairDetailPropertyContentViewHolder extends BaseRecyclerViewHolder<PropertyRepairDetailPropertyContentData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_detail_property_content;

    public PropertyRepairDetailPropertyContentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyRepairDetailPropertyContentData data) {
        if (data == null) return;
        setText(R.id.tv_property_content, data.getContent());
        setImageSrc(R.id.iv_avatar, data.getResId());
    }
}
