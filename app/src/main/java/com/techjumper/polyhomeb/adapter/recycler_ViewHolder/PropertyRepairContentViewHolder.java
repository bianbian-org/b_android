package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.ResourceUtils;
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
        setVisibility(R.id.iv_dot, data.isRead() ? View.INVISIBLE : View.VISIBLE);

        if (data.getBtnName().equals("已完成")) {
            getView(R.id.btn).setEnabled(false);
            ((TextView) getView(R.id.btn)).setTextColor(ResourceUtils.getColorResource(R.color.color_acacac));
        } else {
            getView(R.id.btn).setEnabled(true);
            ((TextView) getView(R.id.btn)).setTextColor(ResourceUtils.getColorResource(R.color.color_37a991));
        }

    }
}
