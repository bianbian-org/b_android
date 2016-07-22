package com.techjumper.polyhome.b.property.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.event.PropertyMessageDetailEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.utils.TypeUtil;

/**
 * Created by kevin on 16/5/12.
 */
@DataBean(beanName = "InfoRepairEntityBean", data = RepairEntity.RepairDataEntity.class)
public class RepairViewHolder extends BaseRecyclerViewHolder<RepairEntity.RepairDataEntity> {

    public static final int LAYOUT_ID = R.layout.item_info;

    public RepairViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(RepairEntity.RepairDataEntity data) {
        if (data == null)
            return;
        long id = data.getId();
        String title = TypeUtil.getRepairTitle(data.getRepair_type(), data.getRepair_device());
        String content = data.getNote();
        String date = data.getRepair_date();

        int status = data.getStatus();


        setText(R.id.info_title, title);
        if (TextUtils.isEmpty(content)) {
            setVisibility(R.id.info_content, View.GONE);
        } else {
            setVisibility(R.id.info_content, View.VISIBLE);
            setText(R.id.info_content, content);
        }

        setText(R.id.info_date, date.substring(0, 10));

        TextView typeTextView = getView(R.id.info_type);
        typeTextView.setVisibility(View.VISIBLE);

        if (status == Constant.STATUS_RESPONSE) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_20c3f3);
            typeTextView.setText(R.string.property_type_response);
        } else if (status == Constant.STATUS_SUBMIT) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
            typeTextView.setText(R.string.property_type_submit);
        } else if (status == Constant.STATUS_FINISH) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            typeTextView.setText(R.string.property_type_finish);
        } else {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            typeTextView.setText(R.string.property_type_close);
        }

        setOnItemClickListener(v -> {
            RxBus.INSTANCE.send(new PropertyMessageDetailEvent(id, PropertyMessageDetailEvent.REPAIR));
        });
    }
}
