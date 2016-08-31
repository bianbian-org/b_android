package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ChooseFamilyVillageEvent;
import com.techjumper.polyhomeb.mvp.m.HomeMenuFragmentModel;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.fragment.HomeMenuFragment;
import com.techjumper.polyhomeb.other.CircleTransform;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.user.event.LoginEvent;

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
    private Subscription mSubs1, mSubs2;

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
                        setAvatarAndName(login);
                    }
                }));
        changeRightText();
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


    @OnClick(R.id.layout_head)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_head:
                if (!UserManager.INSTANCE.isLogin()) {
                    new AcHelper.Builder(getView()
                            .getActivity())
                            .target(LoginActivity.class)
                            .start();
                }
                break;
        }
    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    public void setAvatarAndName(boolean isLogin) {
        getView().getTvUserName().setText(isLogin ? (UserManager.INSTANCE.getUserNickName()) : getView().getActivity().getString(R.string.click_to_login_in));
        String avatarUrl = UserManager.INSTANCE.getUserInfo(UserManager.KEY_AVATAR);
        if (isLogin) {
            if (!TextUtils.isEmpty(avatarUrl)) {
                PicassoHelper.load(avatarUrl).transform(new CircleTransform()).into(getView().getIvAvatar());
            } else {
                PicassoHelper.load(R.mipmap.icon_avatar_too_handsome).transform(new CircleTransform()).into(getView().getIvAvatar());
            }
        } else {
            PicassoHelper.load(R.mipmap.icon_avatar_not_login).transform(new CircleTransform()).into(getView().getIvAvatar());
        }
    }
}
