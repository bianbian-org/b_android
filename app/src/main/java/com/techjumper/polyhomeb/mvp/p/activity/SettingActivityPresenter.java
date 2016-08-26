package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.SettingActivity;
import com.techjumper.polyhomeb.user.UserManager;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SettingActivityPresenter extends AppBaseActivityPresenter<SettingActivity> {
    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick(R.id.tv_logout)
    public void onClick() {
        logout();
    }

    private void logout() {
        DialogUtils.getBuilder(getView())
                .content(R.string.confirm_logout)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            UserManager.INSTANCE.logout();
                            new AcHelper.Builder(getView())
                                    .target(LoginActivity.class)
                                    .closeCurrent(true)
                                    .start();
                            break;
                    }
                })
                .show();
    }
}
