package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.DnakeData;
import com.techjumper.polyhomeb.mvp.v.activity.DnakeLockActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "DnakeBean", data = DnakeData.class)
public class DnakeDoorViewHolder extends BaseRecyclerViewHolder<DnakeData> {

    public static final int LAYOUT_ID = R.layout.item_dnake_door;

    public DnakeDoorViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(DnakeData data) {
        if (data == null) return;

        if (!data.isCommunitySupportDnakeDoor()) {
            setVisibility(R.id.root, View.GONE);
        } else {
            setVisibility(R.id.root, View.VISIBLE);
        }

        setOnClickListener(R.id.root, v ->
                new AcHelper.Builder((Activity) getContext()).target(DnakeLockActivity.class).start());
    }
}
