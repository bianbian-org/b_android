package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailProprietorContentData;
import com.techjumper.polyhomeb.entity.event.ResendMessageEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PropertyRepairDetailProprietorContentBean", data = PropertyRepairDetailProprietorContentData.class)
public class PropertyRepairDetailProprietorContentViewHolder extends BaseRecyclerViewHolder<PropertyRepairDetailProprietorContentData> {

    public static final int LAYOUT_ID = R.layout.item_property_repair_detail_proprietor_content;
    private boolean mCanClick = false;  //发送中时,不能点击菊花,只有当显示为感叹号的时候才能点击

    public PropertyRepairDetailProprietorContentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PropertyRepairDetailProprietorContentData data) {
        if (data == null) return;
        setText(R.id.tv_proprietor_content, data.getContent());
        processStatus(data);
        if (mCanClick) {
            setOnClickListener(R.id.iv_send_failed, v -> DialogUtils.getBuilder((Activity) getContext())
                    .content(R.string.confirm_resend)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onAny((dialog, which) -> {
                        switch (which) {
                            case POSITIVE:
                                RxBus.INSTANCE.send(new ResendMessageEvent(data.getContent(), getLayoutPosition(), data));
                                break;
                            case NEGATIVE:
                                break;
                        }
                    })
                    .show());
        }
    }

    private void processStatus(PropertyRepairDetailProprietorContentData data) {
        RotateAnimation animation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ImageView view = getView(R.id.iv_send_failed);
        switch (data.getSendStatus()) {
            case Constant.MESSAGE_SENDING:
                mCanClick = false;
                view.setImageResource(R.mipmap.icon_ju_hua);
                view.setVisibility(View.VISIBLE);
                startAnimation(view, animation);
                break;
            case Constant.MESSAGE_SEND_FAILED:
                mCanClick = true;
                stopAnimation(view, animation);
                view.setImageResource(R.mipmap.icon_send_failed);
                view.setVisibility(View.VISIBLE);
                break;
            case Constant.MESSAGE_SEND_SUCCESS:
                mCanClick = false;
                stopAnimation(view, animation);
                view.setVisibility(View.GONE);
                break;
        }
    }

    private void startAnimation(ImageView view, RotateAnimation animation) {
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1000);
        animation.setRepeatCount(10);
        view.clearAnimation();
        view.startAnimation(animation);
    }

    private void stopAnimation(ImageView view, RotateAnimation animation) {
        view.clearAnimation();
        animation.cancel();
    }
}
