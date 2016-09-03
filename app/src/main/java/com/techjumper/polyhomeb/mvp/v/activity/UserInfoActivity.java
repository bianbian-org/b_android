package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.UserInfoActivityPresenter;
import com.techjumper.polyhomeb.other.PicassoCircleTransform;
import com.techjumper.polyhomeb.user.UserManager;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(UserInfoActivityPresenter.class)
public class UserInfoActivity extends AppBaseActivity<UserInfoActivityPresenter> {

    @Bind(R.id.tv_right)
    TextView mTvRight;     //右上角文字
    @Bind(R.id.right_group)
    ViewGroup mLayoutRight;//右上角父布局
    @Bind(R.id.tv_nick_name)
    TextView mTvNickName;  //昵称
    @Bind(R.id.tv_email)
    TextView mTvEmail;     //邮箱
    @Bind(R.id.tv_birthday)
    TextView mTvBirthday;  //生日
    @Bind(R.id.tv_sex)
    TextView mTvSex;       //性别
    @Bind(R.id.iv_avatar)
    ImageView mIvAvatar;   //头像
    @Bind(R.id.iv_bg)
    ImageView mIvBg;       //头像背景

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_user_info);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initDatas();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.user_info);
    }

    @Override
    protected boolean showTitleRight() {
        return true;
    }

    @Override
    protected boolean onTitleRightClick() {
        getPresenter().onTitleRightClick();
        return true;
    }

    private void initDatas() {
        String avatar = UserManager.INSTANCE.getUserInfo(UserManager.KEY_AVATAR);
        String birthday = UserManager.INSTANCE.getUserInfo(UserManager.KEY_BIRTHDAY);
        String nickName = UserManager.INSTANCE.getUserInfo(UserManager.KEY_USER_NAME);
        String email = UserManager.INSTANCE.getUserInfo(UserManager.KEY_EMAIL);
        String sex = UserManager.INSTANCE.getUserInfo(UserManager.KEY_SEX);
        if (!TextUtils.isEmpty(sex)) {
            if ("1".equals(sex)) {
                sex = getString(R.string.male);
            } else if ("2".equals(sex)) {
                sex = getString(R.string.female);
            }
        }
        mTvNickName.setText(nickName);
        mTvEmail.setText(email);
        mTvBirthday.setText(birthday);
        mTvSex.setText(sex);

        if (!TextUtils.isEmpty(avatar)) {
            PicassoHelper.load(avatar).transform(new PicassoCircleTransform()).into(mIvAvatar);
            PicassoHelper.load(R.mipmap.icon_avatar_bg).transform(new PicassoCircleTransform()).into(mIvBg);
        } else {
            PicassoHelper.load(R.mipmap.icon_avatar_too_handsome).transform(new PicassoCircleTransform()).into(mIvAvatar);
            PicassoHelper.load(R.mipmap.icon_avatar_bg).transform(new PicassoCircleTransform()).into(mIvBg);
        }

        //最开始的时候,右上角的按钮是灰色且不可点击
        canRightClick(false);
    }

    //这里来控制View
    public void canRightClick(boolean canRightClick) {
        mTvRight.setTextColor(ResourceUtils.getColorResource(canRightClick ? R.color.color_ffffff : R.color.color_70ffffff));
        mLayoutRight.setEnabled(canRightClick);
    }

    public TextView getTvNickName() {
        return mTvNickName;
    }

    public TextView getTvEmail() {
        return mTvEmail;
    }

    public TextView getTvBirthday() {
        return mTvBirthday;
    }

    public TextView getTvSex() {
        return mTvSex;
    }

    public ImageView getIvAvatar() {
        return mIvAvatar;
    }

    public ImageView getIvBg() {
        return mIvBg;
    }

}
