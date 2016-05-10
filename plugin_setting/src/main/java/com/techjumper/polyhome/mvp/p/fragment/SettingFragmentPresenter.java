package com.techjumper.polyhome.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.polyhome.R;
import com.techjumper.polyhome.UserManager;
import com.techjumper.polyhome.mvp.v.fragment.SettingFragment;

import butterknife.OnClick;

/**
 * Created by kevin on 16/5/10.
 */
public class SettingFragmentPresenter extends AppBaseFragmentPresenter<SettingFragment> {

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick(R.id.logout)
    void logout_tv() {
        logout();
    }

    public void logout() {
        DialogUtils.getBuilder(getView().getActivity())
                .content(R.string.confirm_logout)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            UserManager.INSTANCE.logout();
                            UserManager.INSTANCE.notifyLoginOrLogoutEvent(false);
                            break;
                    }
                })
                .show();
    }
}
