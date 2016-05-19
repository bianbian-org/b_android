package com.techjumper.polyhome.b.property.viewholder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.commonres.entity.ReplyEntity;
import com.techjumper.polyhome.b.property.R;

/**
 * Created by kevin on 16/5/19.
 */
@DataBean(beanName = "InfoReplyRightEntityBean", data = ReplyEntity.class)
public class MessageRightViewHolder extends BaseRecyclerViewHolder<ReplyEntity> {

    public static final int LAYOUT_ID = R.layout.item_message_right;

    public MessageRightViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ReplyEntity data) {
        if (data == null)
            return;

        String message = data.getContent();
        String time = data.getTime();

        setText(R.id.name, getContext().getString(R.string.property_me));
        setText(R.id.message, message);
        setText(R.id.date, time);
    }
}
