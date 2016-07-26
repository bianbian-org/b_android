package com.techjumper.polyhome.b.info.viewholder;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.commonres.entity.NoticeEntity;
import com.techjumper.commonres.entity.event.InfoDetailEvent;
import com.techjumper.commonres.entity.event.PropertyMessageDetailEvent;
import com.techjumper.commonres.entity.event.ReadMessageEvent;
import com.techjumper.commonres.util.PluginEngineUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.info.R;
import com.techjumper.polyhome.b.info.UserInfoManager;
import com.techjumper.polyhome.b.info.mvp.v.activity.ShoppingActivity;

/**
 * Created by kevin on 16/5/4.
 */
@DataBean(beanName = "InfoEntityBean", data = InfoEntity.InfoResultEntity.InfoItemEntity.class)
public class InfoEntityViewHolder extends BaseRecyclerViewHolder<InfoEntity.InfoResultEntity.InfoItemEntity> {
    public static final int LAYOUT_ID = R.layout.item_info;
    int hasRead;

    public InfoEntityViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(InfoEntity.InfoResultEntity.InfoItemEntity data) {
        if (data == null)
            return;

        long id = data.getId();
        long object_id = data.getObj_id();
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
        if (type == NoticeEntity.SYSTEM) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_20c3f3);
            typeTextView.setText(R.string.info_system);
        } else if (type == NoticeEntity.ORDER) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
            typeTextView.setText(R.string.info_order);
        } else if (type == NoticeEntity.MEDICAL) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            typeTextView.setText(R.string.info_medical);
        }

        setOnItemClickListener(v -> {
            if (type == NoticeEntity.REPAIR) {
                if (hasRead == InfoEntity.HASREAD_FALSE) {
                    setVisibility(R.id.info_isread, View.INVISIBLE);
                    hasRead = InfoEntity.HASREAD_TURE;
                    RxBus.INSTANCE.send(new ReadMessageEvent(id, type));
                }
                PluginEngineUtil.startPropertyDetail(UserInfoManager.getFamilyId(), UserInfoManager.getUserId(), UserInfoManager.getTicket(), PropertyMessageDetailEvent.REPAIR, object_id);
            } else if (type == NoticeEntity.SUGGEST) {
                if (hasRead == InfoEntity.HASREAD_FALSE) {
                    setVisibility(R.id.info_isread, View.INVISIBLE);
                    hasRead = InfoEntity.HASREAD_TURE;
                    RxBus.INSTANCE.send(new ReadMessageEvent(id, type));
                }
                PluginEngineUtil.startPropertyDetail(UserInfoManager.getFamilyId(), UserInfoManager.getUserId(), UserInfoManager.getTicket(), PropertyMessageDetailEvent.COMPLAINT, object_id);
            } else if (type == NoticeEntity.ORDER) {
                if (hasRead == InfoEntity.HASREAD_FALSE) {
                    setVisibility(R.id.info_isread, View.INVISIBLE);
                    hasRead = InfoEntity.HASREAD_TURE;
                    RxBus.INSTANCE.send(new ReadMessageEvent(id, type));
                }
                Intent intent = new Intent(getContext(), ShoppingActivity.class);
                intent.putExtra(ShoppingActivity.ID, object_id);
                getContext().startActivity(intent);
            } else {
                RxBus.INSTANCE.send(new InfoDetailEvent(title, content, type, date));
                if (hasRead == InfoEntity.HASREAD_FALSE) {
                    setVisibility(R.id.info_isread, View.INVISIBLE);
                    hasRead = InfoEntity.HASREAD_TURE;
                    RxBus.INSTANCE.send(new ReadMessageEvent(id, type));
                }
            }
        });
    }
}
