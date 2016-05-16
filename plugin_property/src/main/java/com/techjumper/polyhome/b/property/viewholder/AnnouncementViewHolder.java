package com.techjumper.polyhome.b.property.viewholder;


import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.hehe.AnnounHehe;

/**
 * Created by kevin on 16/5/12.
 */
@DataBean(beanName = "InfoAnnounHeheBean", data = AnnounHehe.class)
public class AnnouncementViewHolder extends BaseRecyclerViewHolder<AnnounHehe> {

    public static final int LAYOUT_ID = R.layout.item_info;

    public AnnouncementViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(AnnounHehe data) {
        if (data == null)
            return;

        String title = data.getTitle();
        String content = data.getContent();
        String date = data.getDate();

        int hasRead = data.getHasRead();

        if (hasRead == AnnounHehe.HASREAD_TURE) {
            setVisibility(R.id.info_isread, View.INVISIBLE);
        } else if (hasRead == AnnounHehe.HASREAD_FALSE) {
            setVisibility(R.id.info_isread, View.VISIBLE);
        }

        setText(R.id.info_title, title);
        setText(R.id.info_content, content);
        setText(R.id.info_date, date);

        setOnItemClickListener(v -> {
            RxBus.INSTANCE.send(new PropertyNormalDetailEvent(title, date, content));
        });
    }
}
