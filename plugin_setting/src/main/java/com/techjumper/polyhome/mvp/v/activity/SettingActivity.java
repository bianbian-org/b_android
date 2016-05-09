package com.techjumper.polyhome.mvp.v.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.R;
import com.techjumper.polyhome.mvp.p.activity.SettingActivityPresenter;
import com.techjumper.polyhome.mvp.v.fragment.LoginFragment;

/**
 * Created by kevin on 16/5/6.
 */
@Presenter(SettingActivityPresenter.class)
public class SettingActivity extends AppBaseActivity<SettingActivityPresenter> {

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.layout_setting);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        replaceFragment(R.id.container, LoginFragment.getInstance());
    }
}
