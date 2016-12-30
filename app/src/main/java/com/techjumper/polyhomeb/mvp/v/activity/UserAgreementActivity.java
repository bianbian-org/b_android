package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.interfaces.IWebViewTitleClick;
import com.techjumper.polyhomeb.manager.WebTitleManager;
import com.techjumper.polyhomeb.mvp.p.activity.UserAgreementActivityPresenter;
import com.techjumper.polyhomeb.utils.WebTitleHelper;
import com.techjumper.polyhomeb.widget.AdvancedWebView;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(UserAgreementActivityPresenter.class)
public class UserAgreementActivity extends AppBaseWebViewActivity<UserAgreementActivityPresenter>
        implements IWebViewTitleClick {

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_user_agreement);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String url = Config.sUserAgreement;
        new WebTitleManager(url, mViewRoot, this);
        initWebView((AdvancedWebView) findViewById(R.id.wb));
        getWebView().loadUrl(url);
    }

    @Override
    protected boolean isWebViewActivity() {
        return true;
    }

    @Override
    public void onTitleLeftFirstClick(String leftFirstMethod) {
        switch (leftFirstMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onTitleLeftSecondClick(String leftSecondMethod) {

    }

    @Override
    public void onTitleRightFirstClick(String rightFirstMethod) {

    }

    @Override
    public void onTitleRightSecondClick(String rightSecondMethod) {

    }
}
