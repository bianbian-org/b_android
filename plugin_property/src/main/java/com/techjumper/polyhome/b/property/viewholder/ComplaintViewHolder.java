package com.techjumper.polyhome.b.property.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.hehe.ComplaintHehe;

/**
 * Created by kevin on 16/5/12.
 */
@DataBean(beanName = "InfoComplaintHeheBean", data = ComplaintHehe.class)
public class ComplaintViewHolder extends BaseRecyclerViewHolder<ComplaintHehe> {

    public static final int LAYOUT_ID = R.layout.item_info;

    public ComplaintViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ComplaintHehe data) {
        if (data == null)
            return;

        String title = data.getTitle();
        String content = data.getContent();
        String date = data.getDate();
        int type = data.getType();

        int hasRead = data.getHasRead();

        if (hasRead == ComplaintHehe.HASREAD_TURE) {
            setVisibility(R.id.info_isread, View.INVISIBLE);
        } else if (hasRead == ComplaintHehe.HASREAD_FALSE) {
            setVisibility(R.id.info_isread, View.VISIBLE);
        }

        setText(R.id.info_title, title);
        if (TextUtils.isEmpty(content)) {
            setVisibility(R.id.info_content, View.GONE);
        }else {
            setVisibility(R.id.info_content, View.VISIBLE);
            setText(R.id.info_content, content);
        }
        setText(R.id.info_date, date);

        TextView typeTextView = getView(R.id.info_type);
        typeTextView.setVisibility(View.VISIBLE);

        if (type == ComplaintHehe.TYPE_RESPONSE) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_20c3f3);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_20C3F3));
            typeTextView.setText(R.string.property_type_response);
        } else if (type == ComplaintHehe.TYPE_SUBMIT) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_FF9938));
            typeTextView.setText(R.string.property_type_submit);
        } else if (type == ComplaintHehe.TYPE_FINISH) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_4EB738));
            typeTextView.setText(R.string.property_type_finish);
        }

        setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
