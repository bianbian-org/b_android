package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.Manifest;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.ReplyCommentPhotoViewDefaultPlusData;

import me.iwf.photopicker.PhotoPicker;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/16
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "ReplyCommentPhotoViewDefaultPlusBean", data = ReplyCommentPhotoViewDefaultPlusData.class)
public class ReplyPhotoViewDefaultAddViewHolder extends BaseRecyclerViewHolder<ReplyCommentPhotoViewDefaultPlusData> {

    public static final int LAYOUT_ID = R.layout.item_new_repair_photo_view_default_plus;

    public ReplyPhotoViewDefaultAddViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ReplyCommentPhotoViewDefaultPlusData data) {
        if (data == null) return;
        ((ImageView) getView(R.id.iv)).setImageResource(data.getImageResource());

        RxView.clicks(getView(R.id.iv))
                .compose(RxPermissions.getInstance(getContext()).ensure(Manifest.permission.CAMERA))
                .subscribe(granted -> {
                    if (granted) {
                        jumpToPhotoPickerActivity(data);
                    } else {
                        ToastUtils.show(getContext().getString(R.string.no_camera_permission));
                    }
                });
    }

    private void jumpToPhotoPickerActivity(ReplyCommentPhotoViewDefaultPlusData data) {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setSelected(data.getSelectPhotoes())
                .setPreviewEnabled(false)
                .start((Activity) getContext(), PhotoPicker.REQUEST_CODE);
    }
}