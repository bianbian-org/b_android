package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.lightwidget.textview.AutofitTextView;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.ChooseVillageFamilyData;
import com.techjumper.polyhomeb.entity.event.ChooseVillageEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "ChooseVillageFamilyBean", data = ChooseVillageFamilyData.class)
public class ChooseVillageFamilyViewHolder extends BaseRecyclerViewHolder<ChooseVillageFamilyData> {

    public static final int LAYOUT_ID = R.layout.item_choose_village_family;

    public ChooseVillageFamilyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ChooseVillageFamilyData data) {
        if (data == null) return;

//        setText(R.id.tv_province, data.getName());
//        TextView textView = getView(R.id.tv_province);


        AutofitTextView tv = getView(R.id.tv_province);
        tv.setSizeToFit(true);
        tv.setText(data.getName());


        tv.setTextColor(data.isChoosed() ? ResourceUtils.getColorResource(R.color.color_37a991) : ResourceUtils.getColorResource(R.color.color_727272));
        setOnClickListener(R.id.layout_province, v -> {
            tv.setTextColor(ResourceUtils.getColorResource(R.color.color_37a991));
            RxBus.INSTANCE.send(new ChooseVillageEvent(data.getName()));
        });
    }
}
