package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.lightwidget.textview.MarqueeTextView;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyComplainContentData;
import com.techjumper.polyhomeb.mvp.v.activity.ComplainDetailActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyComplainContentBean", data = PropertyComplainContentData.class)
public class PropertyComplainContentViewHolder extends BaseRecyclerViewHolder<PropertyComplainContentData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_content;

    public PropertyComplainContentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyComplainContentData data) {
        if (data == null) return;
        ((MarqueeTextView) getView(R.id.tv_notice)).setText(data.getTitle());
        setText(R.id.btn, data.getBtnName());
        setText(R.id.tv_content, data.getContent());
        setText(R.id.tv_time, data.getTime());
//        setVisibility(R.id.iv_dot, data.isRead() ? View.INVISIBLE : View.VISIBLE);
        setVisibility(R.id.iv_dot, View.INVISIBLE);

        switch (data.getStatus()) {
            case 0:   //未处理
                getView(R.id.btn).setEnabled(true);
                ((TextView) getView(R.id.btn)).setTextColor(ResourceUtils.getColorResource(R.color.color_37a991));
                break;
            case 1:  //已回复
                getView(R.id.btn).setEnabled(true);
                ((TextView) getView(R.id.btn)).setTextColor(ResourceUtils.getColorResource(R.color.color_37a991));
                break;
            case 2:  //已处理
                getView(R.id.btn).setEnabled(false);
                ((TextView) getView(R.id.btn)).setTextColor(ResourceUtils.getColorResource(R.color.color_acacac));
                break;
            case 3:  //已关闭
                getView(R.id.btn).setEnabled(false);
                ((TextView) getView(R.id.btn)).setTextColor(ResourceUtils.getColorResource(R.color.color_acacac));
                break;
        }

        setOnClickListener(R.id.layout_content, v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.PROPERTY_COMPLAIN_DATA_ID, data.getId());
            new AcHelper.Builder((Activity) getContext()).target(ComplainDetailActivity.class).extra(bundle).start();
//            new AcHelper.Builder((Activity) getContext()).extra(bundle).target(TestActivity.class).start();
        });

    }
}
