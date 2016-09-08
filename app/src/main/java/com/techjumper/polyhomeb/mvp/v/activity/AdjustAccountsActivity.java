package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.mvp.p.activity.AdjustAccountsActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(AdjustAccountsActivityPresenter.class)
public class AdjustAccountsActivity extends AppBaseActivity<AdjustAccountsActivityPresenter> {
    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
