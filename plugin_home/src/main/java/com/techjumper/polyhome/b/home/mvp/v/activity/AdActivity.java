package com.techjumper.polyhome.b.home.mvp.v.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.p.activity.AdActivityPresenter;

/**
 * Created by kevin on 16/4/29.
 */
@Presenter(AdActivityPresenter.class)
public class AdActivity extends AppBaseActivity<AdActivityPresenter> {
    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.layout_ad);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }
}
