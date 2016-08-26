package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.SettingActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(SettingActivityPresenter.class)
public class SettingActivity extends AppBaseActivity<SettingActivityPresenter> {

    @Bind(R.id.tv_clear_cache)
    TextView mTvClearCache;   //清除缓存
    @Bind(R.id.tv_appraise)
    TextView mTvAppraise;    //评价
    @Bind(R.id.tv_about)
    TextView mTvAbout;       //关于

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_setting);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.settings);
    }
}
