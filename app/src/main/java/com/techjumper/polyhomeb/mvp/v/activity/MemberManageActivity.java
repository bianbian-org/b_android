package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.mvp.p.activity.MemberManageActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MemberManageActivityPresenter.class)
public class MemberManageActivity extends AppBaseActivity<MemberManageActivityPresenter> {
    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
