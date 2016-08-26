package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.text.TextUtils;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.HomeMenuItemData;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "HomeMenuItemBean", data = HomeMenuItemData.class)
public class HomeMenuItemViewHolder extends BaseRecyclerViewHolder<HomeMenuItemData> {

    public static final int LAYOUT_ID = R.layout.item_home_menu;

    public HomeMenuItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(HomeMenuItemData data) {
        if (data == null) return;
        setText(R.id.title, data.getTitle());
        if (!TextUtils.isEmpty(data.getRightText())) {
            getView(R.id.title_plus).setVisibility(View.VISIBLE);
            setText(R.id.title_plus, data.getRightText());
        } else {
            getView(R.id.title_plus).setVisibility(View.GONE);
        }
        setOnClickListener(R.id.root, v -> {
//            new AcHelper.Builder((Activity) getContext()).target().start();
        });

    }
}
