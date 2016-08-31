package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MessageAllContentData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MessageAllContentData.class, beanName = "MessageAllContentBean")
public class MessageAllContentViewHolder extends BaseRecyclerViewHolder<MessageAllContentData> {

    public static final int LAYOUT_ID = R.layout.item_message_all_content;

    public MessageAllContentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MessageAllContentData data) {
        if (data == null) return;
        setText(R.id.tv_notice, data.getTitle());
        setText(R.id.btn, data.getRightText());
        setText(R.id.tv_content, data.getContent());
        setText(R.id.tv_time, data.getTime());

    }
}
