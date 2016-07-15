package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.lightwidget.textview.MarqueeTextView;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairContentData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyRepairContentBean", data = PropertyRepairContentData.class)
public class PropertyRepairContentViewHolder extends BaseRecyclerViewHolder<PropertyRepairContentData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_content;

    public PropertyRepairContentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyRepairContentData data) {
        if (data == null) return;
        ((MarqueeTextView) getView(R.id.tv_notice)).setText(data.getTitle());
        setText(R.id.btn, data.getBtnName());
        setText(R.id.tv_content, data.getContent());
        setText(R.id.tv_time, data.getTime());
        setVisibility(R.id.iv_dot, data.isRead() ? View.GONE : View.VISIBLE);
    }
}
