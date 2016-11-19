package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MessagePropertyData;
import com.techjumper.polyhomeb.mvp.v.activity.ComplainDetailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.RepairDetailActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/18
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MessagePropertyData.class, beanName = "MessagePropertyBean")
public class MessagePropertyViewHolder extends BaseRecyclerViewHolder<MessagePropertyData> {

    public static final int LAYOUT_ID = R.layout.item_message_all_content;

    public MessagePropertyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MessagePropertyData data) {
        if (data == null) return;
        setText(R.id.tv_notice, data.getTitle());
        setText(R.id.btn, data.getRightText());
        setText(R.id.tv_content, data.getContent());
        setText(R.id.tv_time, data.getTime());
        View view = getView(R.id.root);

        if (TextUtils.isEmpty(data.getObj_id())) {
            view.setBackgroundColor(ResourceUtils.getColorResource(R.color.color_acacac));
            view.setClickable(false);
            view.setEnabled(false);
            return;
        } else {
            setOnClickListener(R.id.root, v -> {
                switch (data.getType()) {
                    case "4":
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constant.PROPERTY_REPAIR_DATA_ID, Integer.parseInt(data.getObj_id()));
                        new AcHelper.Builder((Activity) getContext()).extra(bundle).target(RepairDetailActivity.class).start();
                        break;
                    case "5":
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt(Constant.PROPERTY_COMPLAIN_DATA_ID, Integer.parseInt(data.getObj_id()));
                        new AcHelper.Builder((Activity) getContext()).extra(bundle1).target(ComplainDetailActivity.class).start();
                        break;
                }
            });
        }
    }
}
