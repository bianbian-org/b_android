package com.techjumper.polyhome.b.property.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.hehe.ComplaintHehe;
import com.techjumper.polyhome.b.property.utils.TypeUtil;

/**
 * Created by kevin on 16/5/12.
 */
@DataBean(beanName = "InfoComplaintEntityBean", data = ComplaintEntity.ComplaintDataEntity.class)
public class ComplaintViewHolder extends BaseRecyclerViewHolder<ComplaintEntity.ComplaintDataEntity> {

    public static final int LAYOUT_ID = R.layout.item_info;

    public ComplaintViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ComplaintEntity.ComplaintDataEntity data) {
        if (data == null)
            return;

        String title = TypeUtil.getComplanitTypeString(data.getTypes());
        String content = data.getContent();

        setText(R.id.info_title, title);
        if (TextUtils.isEmpty(content)) {
            setVisibility(R.id.info_content, View.GONE);
        }else {
            setVisibility(R.id.info_content, View.VISIBLE);
            setText(R.id.info_content, content);
        }

        int status = data.getStatus();

        TextView typeTextView = getView(R.id.info_type);
        typeTextView.setVisibility(View.VISIBLE);

        if (status == ComplaintHehe.TYPE_RESPONSE) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_20c3f3);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_20C3F3));
            typeTextView.setText(R.string.property_type_response);
        } else if (status == ComplaintHehe.TYPE_SUBMIT) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_FF9938));
            typeTextView.setText(R.string.property_type_submit);
        } else if (status == ComplaintHehe.TYPE_FINISH) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_4EB738));
            typeTextView.setText(R.string.property_type_finish);
        } else {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_4EB738));
            typeTextView.setText(R.string.property_type_nothing);
        }

        setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
