package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailProprietorContentData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyRepairDetailProprietorContentBean", data = PropertyRepairDetailProprietorContentData.class)
public class PropertyRepairDetailProprietorContentViewHolder extends BaseRecyclerViewHolder<PropertyRepairDetailProprietorContentData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_detail_proprietor_content;

    public PropertyRepairDetailProprietorContentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyRepairDetailProprietorContentData data) {
        if (data == null) return;
        setText(R.id.tv_proprietor_content, data.getContent());
        getView(R.id.iv_send_failed).setVisibility(data.isFailed() ? View.VISIBLE : View.GONE);
        setOnClickListener(R.id.iv_send_failed, v -> DialogUtils.getBuilder((Activity) getContext())
                .content(R.string.confirm_resend)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            ToastUtils.show("重新发送");
                            break;
                        case NEGATIVE:
                            ToastUtils.show("不重新发送");
                            break;
                    }
                })
                .show());
    }
}
