package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyDataBean", data = PropertyData.class)
public class PropertyViewHolder extends BaseRecyclerViewHolder<PropertyData> {

    public static final int LAYOUT_ID = R.layout.item_home_page_property;

    public PropertyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyData data) {
        if (data == null)
            return;
        setText(R.id.tv_notice, data.getNotice());
        setOnClickListener(R.id.btn_checkout_property, v -> {
            ToastUtils.show("点击了物业的查看");
        });
        setOnClickListener(R.id.repair, v -> ToastUtils.show("点击了维修"));
    }
}
