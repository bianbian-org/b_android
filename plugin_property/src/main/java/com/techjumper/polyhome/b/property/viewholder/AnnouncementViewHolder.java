package com.techjumper.polyhome.b.property.viewholder;


import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.utils.StringUtil;

/**
 * Created by kevin on 16/5/12.
 */
@DataBean(beanName = "InfoAnnouncementEntityBean", data = AnnouncementEntity.AnnouncementDataEntity.class)
public class AnnouncementViewHolder extends BaseRecyclerViewHolder<AnnouncementEntity.AnnouncementDataEntity> {

    public static final int LAYOUT_ID = R.layout.item_info;

    public AnnouncementViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(AnnouncementEntity.AnnouncementDataEntity data) {
        if (data == null)
            return;

        String title = data.getTitle();
        String content = data.getContent();
        String date = data.getTime();

        setText(R.id.info_title, title);
        setText(R.id.info_content, StringUtil.delHTMLTag(content));
        setText(R.id.info_date, date);

        setOnItemClickListener(v -> {
            RxBus.INSTANCE.send(new PropertyNormalDetailEvent(title, date, content));
        });
    }
}
