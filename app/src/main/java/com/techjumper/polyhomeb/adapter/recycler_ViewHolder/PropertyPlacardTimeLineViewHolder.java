package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardTimeLineData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyPlacardTimeLineBean", data = PropertyPlacardTimeLineData.class)
public class PropertyPlacardTimeLineViewHolder extends BaseRecyclerViewHolder<PropertyPlacardTimeLineData> {

    public static final int LAYOUT_ID = R.layout.item_property_placard_time_line;

    public PropertyPlacardTimeLineViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyPlacardTimeLineData data) {
        if (data == null) return;
        setText(R.id.tv_time, data.getTime());
        if (getLayoutPosition() == 0) {
            getView(R.id.view_top).setBackgroundColor(ResourceUtils.getColorResource(R.color.color_ededed));
        } else {
            getView(R.id.view_top).setBackgroundColor(ResourceUtils.getColorResource(R.color.color_0536a990));
        }
    }
}
