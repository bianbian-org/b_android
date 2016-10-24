package com.techjumper.polyhomeb.mvp.p.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.AvatarEvent;
import com.techjumper.polyhomeb.entity.event.ChangeEvent;
import com.techjumper.polyhomeb.entity.event.ChooseFamilyVillageEvent;
import com.techjumper.polyhomeb.mvp.m.HomeMenuFragmentModel;
import com.techjumper.polyhomeb.mvp.v.activity.UserInfoActivity;
import com.techjumper.polyhomeb.mvp.v.fragment.HomeMenuFragment;
import com.techjumper.polyhomeb.other.GlideBitmapTransformation;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.user.event.LoginEvent;
import com.techjumper.polyhomeb.utils.PicUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.OnClick;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeMenuFragmentPresenter extends AppBaseFragmentPresenter<HomeMenuFragment> {

    private HomeMenuFragmentModel mModel = new HomeMenuFragmentModel(this);
    private Subscription mSubs1, mSubs2, mSubs3;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof LoginEvent) {
                        LoginEvent loginEvent = (LoginEvent) o;
                        boolean login = loginEvent.isLogin();
                        setAvatarAndName();
                        if (login) { //主要是因为用户1直接点击退出,此时到了登录界面,用户2登陆了.如果不做这个操作,那么就会导致用户2登陆之后显示的侧边栏家庭小区依然是用户1的
                            //这里和HomeFragmentPresenter中的处理一样
                            getView().getAdapter().loadData(getDatas());
                            getView().getAdapter().notifyDataSetChanged();
                        }
                    } else if (o instanceof ChangeEvent) {
                        ChangeEvent event = (ChangeEvent) o;
                        int type = event.getType();
                        if (1 == type) {
                            getView().getTvUserName().setText(UserManager.INSTANCE.getUserInfo(UserManager.KEY_USER_NAME));
                        } else if (2 == type) {

                        }
                    }
                }));
        changeRightText();
        changeAvatar();
    }

    private void changeRightText() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE
                        .asObservable().subscribe(o -> {
                            if (o instanceof ChooseFamilyVillageEvent) {
                                getView().getAdapter().loadData(getDatas());
                                getView().getAdapter().notifyDataSetChanged();
                            }
                        }));
    }


    @OnClick(R.id.iv_avatar)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
//                if (!UserManager.INSTANCE.isLogin()) {
//                    new AcHelper.Builder(getView()
//                            .getActivity())
//                            .target(LoginActivity.class)
//                            .start();
//                }
                new AcHelper.Builder(getView().getActivity())
                        .target(UserInfoActivity.class)
                        .start();
                break;
        }
    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    public void setAvatarAndName() {
        String userNickName = UserManager.INSTANCE.getUserInfo(UserManager.KEY_USER_NAME);
        String userPhone = UserManager.INSTANCE.getUserInfo(UserManager.KEY_PHONE_NUMBER);
        String avatarUrl = UserManager.INSTANCE.getUserInfo(UserManager.KEY_AVATAR);
        String localAvatarUrl = UserManager.INSTANCE.getUserInfo(UserManager.KEY_LOCAL_AVATAR);

        if (!TextUtils.isEmpty(userNickName)) {
            getView().getTvUserName().setText(userNickName);
        } else {
            getView().getTvUserName().setText(userPhone);
        }

        //优先加载本地的图片,如果不存在才去网络请求
        if (!TextUtils.isEmpty(localAvatarUrl)) {
//            PicassoHelper.load(localAvatarUrl).transform(new PicassoCircleTransform()).into(getView().getIvAvatar());
//            PicassoHelper.load(R.mipmap.icon_avatar_bg).transform(new PicassoCircleTransform()).into(getView().getIvBg());
            Glide.with(getView()).load(localAvatarUrl).transform(new GlideBitmapTransformation(getView().getActivity())).into(getView().getIvAvatar());
            Glide.with(getView()).load(R.mipmap.icon_avatar_bg).transform(new GlideBitmapTransformation(getView().getActivity())).into(getView().getIvBg());
        } else if (!TextUtils.isEmpty(avatarUrl)) {
//            PicassoHelper.load(avatarUrl).transform(new PicassoCircleTransform()).into(getView().getIvAvatar());
//            PicassoHelper.load(R.mipmap.icon_avatar_bg).transform(new PicassoCircleTransform()).into(getView().getIvBg());
            Glide.with(getView()).load(avatarUrl).transform(new GlideBitmapTransformation(getView().getActivity())).into(getView().getIvAvatar());
            Glide.with(getView()).load(R.mipmap.icon_avatar_bg).transform(new GlideBitmapTransformation(getView().getActivity())).into(getView().getIvBg());
            //缓存图片到本地
            new Thread(() -> {
                try {
                    Bitmap bitmap = PicassoHelper.load(avatarUrl).get();
                    String path = PicUtils.savePhoto(
                            bitmap
                            , Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Config.sParentDirName + File.separator + Config.sAvatarsDirName
                            , String.valueOf(System.currentTimeMillis()) + "avatar");
                    if (!TextUtils.isEmpty(path)) {
                        UserManager.INSTANCE.saveUserInfo(UserManager.KEY_LOCAL_AVATAR, path);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void changeAvatar() {
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof AvatarEvent) {
                        String avatarUrl = UserManager.INSTANCE.getUserInfo(UserManager.KEY_AVATAR);
                        setAvatarAndName();
                    }
                })
        );
    }

    public void openStartHomeSetting() {
        ToastUtils.show(Utils.appContext.getString(R.string.error_not_complete));
    }
}
