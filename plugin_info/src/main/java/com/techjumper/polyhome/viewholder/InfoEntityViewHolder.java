package com.techjumper.polyhome.viewholder;

import android.view.View;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.listener.TextWatcherAdapter;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhome.InfoEntityTemporary;
import com.techjumper.polyhome.R;

/**
 * Created by kevin on 16/5/4.
 */
@DataBean(beanName = "InfoEntityBean", data = InfoEntityTemporary.class)
public class InfoEntityViewHolder extends BaseRecyclerViewHolder<InfoEntityTemporary> {
    public static final int LAYOUT_ID = R.layout.item_info;

    public InfoEntityViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(InfoEntityTemporary data) {
        if (data == null)
            return;

        setText(R.id.info_title, data.getTitle());
        setText(R.id.info_content, data.getContent());
        setText(R.id.info_date, data.getDate());
        if (data.isRead()) {
            setVisibility(R.id.info_isread, View.INVISIBLE);
        } else {
            setVisibility(R.id.info_isread, View.VISIBLE);
        }

        TextView typeTextView = getView(R.id.info_type);
        // TODO: 16/5/4 当接口来的时候再抽离出来
        if (data.getType().equals(getContext().getString(R.string.info_system))) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_20c3f3);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_20C3F3));
            typeTextView.setText(R.string.info_system);
        } else if (data.getType().equals(getContext().getString(R.string.info_order))) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_ff9938);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_FF9938));
            typeTextView.setText(R.string.info_order);
        } else if (data.getType().equals(getContext().getString(R.string.info_medical))) {
            typeTextView.setBackgroundResource(R.drawable.bg_shape_radius_4eb738);
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.color_4EB738));
            typeTextView.setText(R.string.info_medical);
        }
    }
}
