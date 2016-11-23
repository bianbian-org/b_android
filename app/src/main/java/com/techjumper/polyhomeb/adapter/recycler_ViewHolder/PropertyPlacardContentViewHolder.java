package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardContentData;
import com.techjumper.polyhomeb.mvp.v.activity.PlacardDetailActivity;

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
        setText(R.id.tv_notice, data.getTitle());
//        setVisibility(R.id.iv_dot, data.isRead() ? View.INVISIBLE : View.VISIBLE);
        setVisibility(R.id.iv_dot, View.INVISIBLE);
        ((Button) getView(R.id.btn)).setText(data.getType());
        setText(R.id.tv_content, data.getContent());
        setText(R.id.tv_time, data.getTime());
        setOnClickListener(R.id.layout_content, v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.PLACARD_DETAIL_ID, data.getId());
            bundle.putString(Constant.PLACARD_DETAIL_CONTENT, data.getContent_());
            bundle.putString(Constant.PLACARD_DETAIL_TIME, data.getTime());
            bundle.putString(Constant.PLACARD_DETAIL_TITLE, data.getTitle());
            bundle.putString(Constant.PLACARD_DETAIL_TYPE, data.getType());
            new AcHelper.Builder((Activity) getContext()).extra(bundle).target(PlacardDetailActivity.class).start();
        });
    }
}
