package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRepairDetailChoosedPicData;
import com.techjumper.polyhomeb.entity.event.PhotoViewEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/28
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "NewRepairDetailChoosedPicBean", data = NewRepairDetailChoosedPicData.class)
public class NewRepairDetailChoosedPicViewHolder extends BaseRecyclerViewHolder<NewRepairDetailChoosedPicData> {

    public static final int LAYOUT_ID = R.layout.item_choosed_pic_form_net;

    public NewRepairDetailChoosedPicViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(NewRepairDetailChoosedPicData data) {
        if (data == null) return;

        Glide.with((Activity) getContext())
                .load(data.getPicUrl())
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.mipmap.picker_ic_photo_black)
                .error(R.mipmap.picker_ic_photo_black)
                .into((ImageView) getView(R.id.iv));

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getView(R.id.iv).getLayoutParams();
        layoutParams.rightMargin = 20;
        getView(R.id.iv).setLayoutParams(layoutParams);

//        PhotoPreview.builder()
//                .setPhotos(selectedPhotos)
//                .setCurrentItem(position)
//                .start(MainActivity.this);

        // TODO: 16/7/28 加载大图的话,可以使用上面这个,然后进去改改界面,并且将所有的data都存到selectedPhotos里面,也是String的
        setOnClickListener(R.id.iv, v -> RxBus.INSTANCE.send(new PhotoViewEvent(data.getPicUrl())));

    }
}
