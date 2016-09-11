package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PolyHomeData;
import com.techjumper.polyhomeb.mvp.v.activity.CheckInActivity;
import com.techjumper.polyhomeb.widget.PolyModeView;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PolyHomeDataBean", data = PolyHomeData.class)
public class PolyHomeViewHolder extends BaseRecyclerViewHolder<PolyHomeData> {

    public static final int LAYOUT_ID = R.layout.item_home_page_polyhome;

    public PolyHomeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PolyHomeData data) {
        if (data == null) return;

        ((PolyModeView) getView(R.id.hengwen)).setText(data.getSceneName1());
        ((PolyModeView) getView(R.id.anfang)).setText(data.getSceneName2());
        ((PolyModeView) getView(R.id.test3)).setText(data.getSceneName3());
        ((PolyModeView) getView(R.id.test4)).setText(data.getSceneName4());

        setOnClickListener(R.id.layout_check_in, v -> new AcHelper.Builder((Activity) getContext()).target(CheckInActivity.class).start());
    }

}
