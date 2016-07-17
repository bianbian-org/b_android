package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;
import android.widget.Button;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.lightwidget.textview.MarqueeTextView;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardContentData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyPlacardContentBean", data = PropertyPlacardContentData.class)
public class PropertyPlacardContentViewHolder extends BaseRecyclerViewHolder<PropertyPlacardContentData> {

    public static final int LAYOUT_ID = R.layout.item_property_placard_content;

    public PropertyPlacardContentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyPlacardContentData data) {
        if (data == null) return;
        ((MarqueeTextView) getView(R.id.tv_notice)).setText(data.getTitle());
        setVisibility(R.id.iv_dot, data.isRead() ? View.INVISIBLE : View.VISIBLE);
        ((Button) getView(R.id.btn)).setText(data.getBtnName());
        setText(R.id.tv_content, data.getContent());
        setText(R.id.tv_time, data.getTime());
        setOnClickListener(R.id.layout_content, v -> {

        });

    }
}
