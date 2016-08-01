package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;
import android.widget.LinearLayout;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyPlacardDividerBean", data = PropertyPlacardDividerData.class)
public class PropertyPlacardDividerViewHolder extends BaseRecyclerViewHolder<PropertyPlacardDividerData> {

    public static final int LAYOUT_ID = R.layout.item_property_placard_divider;

    public PropertyPlacardDividerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyPlacardDividerData data) {
        if (data == null) return;
        if (data.getMarginLeft() != 0) {
            View view = getView(R.id.view);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            layoutParams.leftMargin = data.getMarginLeft();
            view.setLayoutParams(layoutParams);
        }
    }
}
