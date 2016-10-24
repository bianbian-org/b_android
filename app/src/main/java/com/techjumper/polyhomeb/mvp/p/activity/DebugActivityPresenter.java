package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.manager.PolyPluginManager;
import com.techjumper.polyhomeb.mvp.v.activity.DebugActivity;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class DebugActivityPresenter extends AppBaseActivityPresenter<DebugActivity> {

    private PolyPluginManager mPluginManager;

    @Override
    public void initData(Bundle savedInstanceState) {
        mPluginManager = PolyPluginManager.with(getView());
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn_open_smart_home, R.id.btn_uninstall_smart_home})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_smart_home:
                openSmartHome();
                break;
            case R.id.btn_uninstall_smart_home:
                uninstallSmartHome();
                break;
        }
    }

    private void openSmartHome() {
        mPluginManager.openSmartHome(new PolyPluginManager.IStartPluginListener() {
            @Override
            public void onPluginLoading() {
                getView().showLoading();
            }

            @Override
            public void onPluginInstalling() {
                getView().dismissLoading();
            }

            @Override
            public void onPluginStarted() {
                getView().dismissLoading();
            }

            @Override
            public void onPluginError(Throwable e) {
                getView().dismissLoading();
            }
        });
    }

    private void uninstallSmartHome() {
        mPluginManager.uninstallCPlugin();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPluginManager.quit();
    }
}
