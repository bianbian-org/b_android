package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.mvp.p.activity.TabHomeActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(TabHomeActivityPresenter.class)
public class TabHomeActivity extends AppBaseActivity<TabHomeActivityPresenter> {
    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
