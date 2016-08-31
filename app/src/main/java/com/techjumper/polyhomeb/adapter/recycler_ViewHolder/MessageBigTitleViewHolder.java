package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MessageBigTitleData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MessageBigTitleData.class, beanName = "MessageBigTitleBean")
public class MessageBigTitleViewHolder extends BaseRecyclerViewHolder<MessageBigTitleData> {

    public static final int LAYOUT_ID = R.layout.item_message_big_title;

    public MessageBigTitleViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MessageBigTitleData data) {
        if (data == null) return;
        setText(R.id.tv_title, data.getTitle_name());
        setText(R.id.tv_num, data.getTotal_num() + "");
    }
}
