package com.techjumper.polyhome.viewholder;

import android.view.View;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.listener.TextWatcherAdapter;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.event.InfoDetailEvent;
import com.techjumper.commonres.entity.event.ReadMessageEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.InfoEntityTemporary;
import com.techjumper.polyhome.R;

/**
 * Created by kevin on 16/5/4.
 */
@DataBean(beanName = "InfoEntityBean", data = InfoEntity.InfoDataEntity.InfoItemEntity.class)
public class InfoEntityViewHolder extends BaseRecyclerViewHolder<InfoEntity.InfoDataEntity.InfoItemEntity> {
    public static final int LAYOUT_ID = R.layout.item_info;
    int hasRead;

    public InfoEntityViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(InfoEntity.InfoDataEntity.InfoItemEntity data) {
        if (data == null)
            return;

        long id = data.getId();
        String title = data.getTitle();
        String content = data.getContent();
        String date = data.getCreated_at();

        int type = data.getTypes();
        hasRead = data.getHas_read();

        setText(R.id.info_title, title);
        setText(R.id.info_content, content);
        setText(R.id.info_date, date);


        if (hasRead == InfoEntity.HASREAD_TURE) {
            setVisibility(R.id.info_isread, View.INVISIBLE);
        } else if (hasRead == InfoEntity.HASREAD_FALSE) {
            setVisibility(R.id.info_isread, View.VISIBLE);
        }

        TextView typeTextView = getView(R.id.info_type);
        // TODO: 16/5/4 当接口来的时候再抽离出来
        if (type == InfoEntity.TYPE_SYSTEM) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_20c3f3);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_20C3F3));
            typeTextView.setText(R.string.info_system);
        } else if (type == InfoEntity.TYPE_ORDER) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_FF9938));
            typeTextView.setText(R.string.info_order);
        } else if (type == InfoEntity.TYPE_MEDICAL) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_4EB738));
            typeTextView.setText(R.string.info_medical);
        }

        setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.INSTANCE.send(new InfoDetailEvent(title, content, type, date));
                if (hasRead == InfoEntity.HASREAD_FALSE) {
                    setVisibility(R.id.info_isread, View.INVISIBLE);
                    hasRead = InfoEntity.HASREAD_TURE;
                    RxBus.INSTANCE.send(new ReadMessageEvent(id));
                }
            }
        });
    }
}
