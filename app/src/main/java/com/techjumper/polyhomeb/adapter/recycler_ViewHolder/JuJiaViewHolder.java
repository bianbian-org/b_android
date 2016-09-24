package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.JuJiaData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "JuJiaDataBean", data = JuJiaData.class)
public class JuJiaViewHolder extends BaseRecyclerViewHolder<JuJiaData> {

    public static final int LAYOUT_ID = R.layout.item_home_page_jujia;

    public JuJiaViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(JuJiaData data) {
        if (data == null) return;

        setText(R.id.tv_notice, data.getNotice());


    }
}
