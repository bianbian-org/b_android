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
import com.techjumper.polyhomeb.adapter.recycler_Data.NewInvitationPhotoViewChoosedData;
import com.techjumper.polyhomeb.entity.event.DeletePicNotifyEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/19
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = NewInvitationPhotoViewChoosedData.class, beanName = "NewInvitationPhotoViewChoosedBean")
public class NewInvitationPhotoViewChoosedViewHolder extends BaseRecyclerViewHolder<NewInvitationPhotoViewChoosedData> {

    public static final int LAYOUT_ID = R.layout.item_new_repair_photo_view_default_plus;

    public NewInvitationPhotoViewChoosedViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(NewInvitationPhotoViewChoosedData data) {
        if (data == null) return;
        getView(R.id.iv_delete).setVisibility(View.VISIBLE);
        setOnClickListener(R.id.iv_delete, v -> {
            RxBus.INSTANCE.send(new DeletePicNotifyEvent(getPosition())); //此处在NewRepairActivityPresenter中进行接收
        });
        Glide.with((Activity) getContext())
                .load(data.getMapPath())
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.mipmap.picker_ic_photo_black)
                .error(R.mipmap.picker_ic_photo_black)
                .into((ImageView) getView(R.id.iv));

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getView(R.id.iv).getLayoutParams();
        layoutParams.rightMargin = 20;
        layoutParams.bottomMargin = 20;
        getView(R.id.iv).setLayoutParams(layoutParams);

        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) getView(R.id.iv_delete).getLayoutParams();
        layoutParams1.rightMargin = 20;
        layoutParams1.bottomMargin = 20;
        getView(R.id.iv_delete).setLayoutParams(layoutParams1);

    }

}
