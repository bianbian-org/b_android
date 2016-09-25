package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyData;
import com.techjumper.polyhomeb.mvp.v.activity.PaymentActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PropertyDetailActivity;

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
//            ToastUtils.show("点击了物业的查看");
        });
        //维修
        setOnClickListener(R.id.repair, v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_REPAIR);
            new AcHelper.Builder((Activity) getContext())
                    .target(PropertyDetailActivity.class)
                    .extra(bundle)
                    .start();
        });
        //投诉
        setOnClickListener(R.id.complaint, v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_COMPLAINT);
            new AcHelper.Builder((Activity) getContext())
                    .target(PropertyDetailActivity.class)
                    .extra(bundle)
                    .start();
        });
        //公告
        setOnClickListener(R.id.placard, v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_PLACARD);
            new AcHelper.Builder((Activity) getContext())
                    .target(PropertyDetailActivity.class)
                    .extra(bundle)
                    .start();
        });
        //缴费
        setOnClickListener(R.id.payment, v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.KEY_CURRENT_BUTTON, Constant.VALUE_PAYMENT);
            new AcHelper.Builder((Activity) getContext())
                    .target(PaymentActivity.class)
                    .extra(bundle)
                    .start();
        });
    }
}