package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.m.HomeMenuFragmentModel;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.fragment.HomeMenuFragment;
import com.techjumper.polyhomeb.other.CircleTransform;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.user.event.LoginEvent;

import java.util.List;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeMenuFragmentPresenter extends AppBaseFragmentPresenter<HomeMenuFragment> {

    private HomeMenuFragmentModel mModel = new HomeMenuFragmentModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        addSubscription(RxBus.INSTANCE.asObservable().subscribe(o -> {
            if (o instanceof LoginEvent) {
                LoginEvent loginEvent = (LoginEvent) o;
                boolean login = loginEvent.isLogin();
                setAvatarAndName(login);
            }
        }));
    }

    @OnClick(R.id.layout_head)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_head:
                if (!UserManager.INSTANCE.isLogin()) {
                    new AcHelper.Builder(getView().getActivity()).target(LoginActivity.class).start();
                } else {
//                    new AcHelper.Builder(getView().getActivity()).target(LogoutActivity.class).start();
                    logout();
                }
                break;
        }
    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    private void setAvatarAndName(boolean isLogin) {
        getView().getTvUserName().setText(isLogin ? (UserManager.INSTANCE.getUserNickName()) : getView().getActivity().getString(R.string.click_to_login_in));
//        try {
//            getView().getIvAvatar().setImageBitmap(isLogin ? (PicassoHelper.load(UserManager.INSTANCE.getUserInfo(UserManager.KEY_AVATAR)).get()) : (PicassoHelper.load(R.mipmap.ic_launcher).get()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (isLogin) {
            if (!TextUtils.isEmpty(UserManager.INSTANCE.getUserInfo(UserManager.KEY_AVATAR))) {
                PicassoHelper.load(UserManager.INSTANCE.getUserInfo(UserManager.KEY_AVATAR)).transform(new CircleTransform()).into(getView().getIvAvatar());
            } else {
                PicassoHelper.load(R.mipmap.ic_launcher).into(getView().getIvAvatar());
            }
        } else {
            PicassoHelper.load(R.mipmap.ic_launcher).into(getView().getIvAvatar());
        }
    }

    private void logout() {
        DialogUtils.getBuilder(getView().getActivity())
                .content(R.string.confirm_logout)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            UserManager.INSTANCE.logout();
                            if (getView().getActivity() != null && !getView().getActivity().isFinishing())
                                getView().getActivity().onBackPressed();
                            break;
                    }
                })
                .show();
    }
}
