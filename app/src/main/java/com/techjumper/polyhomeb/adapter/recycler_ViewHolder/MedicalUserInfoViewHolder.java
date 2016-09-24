package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalUserInfoData;
import com.techjumper.polyhomeb.entity.event.MedicalChangeUserInfoEvent;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalUserInfoActivityPresenter;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangUserEmailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeUserInfoActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangUserSexActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = MedicalUserInfoData.class, beanName = "MedicalUserInfoBean")
public class MedicalUserInfoViewHolder extends BaseRecyclerViewHolder<MedicalUserInfoData> {

    public static final int LAYOUT_ID = R.layout.item_medical_user_info;

    public MedicalUserInfoViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MedicalUserInfoData data) {
        if (data == null) return;
        TextView tv_label = getView(R.id.tv_label);
        TextView tv_content = getView(R.id.tv_content);
        if (getLayoutPosition() == 10) {
            String content = data.getContent();
            String sex_;
            if (TextUtils.isEmpty(content)) {
                sex_ = "";
            } else {
                int sex = Integer.parseInt(content);
                if (sex == 1) {
                    sex_ = Utils.appContext.getString(R.string.male);
                } else if (sex == 2) {
                    sex_ = Utils.appContext.getString(R.string.female);
                } else {
                    sex_ = "";
                }
            }
            tv_content.setText(sex_);
        } else {
            tv_content.setText(data.getContent());
        }
        tv_label.setText(data.getLabel());

        if (getLayoutPosition() == 16) {  //手机  手机由于接口不下发短信,只下发json验证码,所以就不能修改手机号
            tv_content.setTextColor(ResourceUtils.getColorResource(R.color.color_e2e2e2));
            tv_label.setTextColor(ResourceUtils.getColorResource(R.color.color_e2e2e2));
        } else {
            tv_content.setTextColor(ResourceUtils.getColorResource(R.color.color_a8a8a8));
            tv_label.setTextColor(ResourceUtils.getColorResource(R.color.color_3e3e3e));
        }

        setOnClickListener(R.id.layout_root, v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(MedicalUserInfoActivityPresenter.POSITION, getLayoutPosition());  //rv中的position
            bundle.putString(MedicalUserInfoActivityPresenter.DATA, data.getContent()); //item上显示的sp的数据
            switch (getLayoutPosition()) {
                case 2:  //rv中的position2代表视图中的昵称,也就是1
                    bundle.putInt(MedicalUserInfoActivityPresenter.TYPE, MedicalUserInfoActivityPresenter.NICKNAME);  //type   MedicalUserInfoActivityPresenter.Nickname
                    new AcHelper.Builder((Activity) getContext()).extra(bundle).target(MedicalChangeUserInfoActivity.class).start();
                    break;
                case 4:  //rv中的4代表视图中的姓名,也就是2
                    bundle.putInt(MedicalUserInfoActivityPresenter.TYPE, MedicalUserInfoActivityPresenter.NAME);
                    new AcHelper.Builder((Activity) getContext()).extra(bundle).target(MedicalChangeUserInfoActivity.class).start();
                    break;
                case 6:  //身份证
                    bundle.putInt(MedicalUserInfoActivityPresenter.TYPE, MedicalUserInfoActivityPresenter.IDCARD);
                    new AcHelper.Builder((Activity) getContext()).extra(bundle).target(MedicalChangeUserInfoActivity.class).start();
                    break;
                case 8:   //生日
                    RxBus.INSTANCE.send(new MedicalChangeUserInfoEvent(MedicalUserInfoActivityPresenter.BIRTHDAY, "", getLayoutPosition()));
                    break;
                case 10:  //性别
                    bundle.putInt(MedicalUserInfoActivityPresenter.TYPE, MedicalUserInfoActivityPresenter.SEX);
                    new AcHelper.Builder((Activity) getContext()).extra(bundle).target(MedicalChangUserSexActivity.class).start();
                    break;
                case 14:   //座机
                    bundle.putInt(MedicalUserInfoActivityPresenter.TYPE, MedicalUserInfoActivityPresenter.HOMEPHONE);
                    new AcHelper.Builder((Activity) getContext()).extra(bundle).target(MedicalChangeUserInfoActivity.class).start();
                    break;
//                case 16:   //手机  手机由于接口不下发短信,只下发json验证码,所以就不能修改手机号
//                    bundle.putInt(MedicalUserInfoActivityPresenter.TYPE, MedicalUserInfoActivityPresenter.MOBILEPHONE);
//                    new AcHelper.Builder((Activity) getContext()).extra(bundle).target(MedicalChangeUserInfoActivity.class).start();
//                    break;
                case 18:   //邮箱
                    bundle.putInt(MedicalUserInfoActivityPresenter.TYPE, MedicalUserInfoActivityPresenter.EMAIL);
                    new AcHelper.Builder((Activity) getContext()).extra(bundle).target(MedicalChangUserEmailActivity.class).start();
                    break;
                case 22:   //身高
                    RxBus.INSTANCE.send(new MedicalChangeUserInfoEvent(MedicalUserInfoActivityPresenter.HEIGHT, "", getLayoutPosition()));
                    break;
                case 24:    //体重
                    RxBus.INSTANCE.send(new MedicalChangeUserInfoEvent(MedicalUserInfoActivityPresenter.WEIGHT, "", getLayoutPosition()));
                    break;
            }
        });
    }
}
