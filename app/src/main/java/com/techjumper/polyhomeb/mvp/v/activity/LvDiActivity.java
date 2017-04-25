package com.techjumper.polyhomeb.mvp.v.activity;


import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.LvDiActivityPresenter;
import com.techjumper.polyhomeb.mvp.v.fragment.LvDiFragment;

@Presenter(LvDiActivityPresenter.class)
public class LvDiActivity extends AppBaseActivity<LvDiActivityPresenter> {

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_lvdi);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            switchFragment(R.id.lvdi_container, LvDiFragment.getInstance(), false, false);
        }
    }
}
