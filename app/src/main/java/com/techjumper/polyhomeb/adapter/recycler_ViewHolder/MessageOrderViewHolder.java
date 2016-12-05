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
import com.techjumper.polyhomeb.adapter.recycler_Data.MessageOrderData;
import com.techjumper.polyhomeb.mvp.v.activity.OrderDetailActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "MessageOrderBean", data = MessageOrderData.class)
public class MessageOrderViewHolder extends BaseRecyclerViewHolder<MessageOrderData> {

    public static final int LAYOUT_ID = R.layout.item_message_all_content;

    public MessageOrderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MessageOrderData data) {
        if (data == null) return;
        setVisibility(R.id.iv_dot, data.getHas_read() == 0 ? View.VISIBLE : View.INVISIBLE);
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
                switch (data.getTypes()) {
                    case "2":
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt(Constant.KEY_ORDER_ID, data.getId());
                        bundle2.putString(Constant.KEY_ORDER_MESSAGE_ID, data.getObj_id());
                        setVisibility(R.id.iv_dot, View.INVISIBLE);
//                        RxBus.INSTANCE.send(new UpdateMessageStateEvent(data.getId()));
                        new AcHelper.Builder((Activity) getContext()).extra(bundle2).target(OrderDetailActivity.class).start();
                        break;
                }
            });
        }
    }
}
