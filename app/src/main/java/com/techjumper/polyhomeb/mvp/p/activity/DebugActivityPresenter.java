package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.system.AppUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.manager.PluginAssetsManager;
import com.techjumper.polyhomeb.manager.PolyPluginManager;
import com.techjumper.polyhomeb.mvp.v.activity.DebugActivity;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

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
                JLog.d("Polyhome B Application: "+ Utils.appContext);
                openSmartHome();
                break;
            case R.id.btn_uninstall_smart_home:
                uninstallSmartHome();
                break;
        }
    }

    private void uninstallSmartHome() {
        if (!mPluginManager.isInstallCPlugin()) {
            ToastUtils.show(Utils.appContext.getString(R.string.do_not_have_smart_home_moudle));
            return;
        }
        mPluginManager.uninstallCPlugin();
    }

    private void openSmartHome() {
        addSubscription(
                RxPermissions.getInstance(getView())
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .filter(aBoolean -> aBoolean != null && aBoolean)
                        .flatMap(aBoolean1 -> {
                            getView().showLoading();
                            return PluginAssetsManager.getInstance().copyAssetsPluginToInstallDir();
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<PluginAssetsManager.CopyEntity>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                ToastUtils.show(Utils.appContext.getString(R.string.cannot_access_sd_card));
                            }

                            @Override
                            public void onNext(PluginAssetsManager.CopyEntity copyEntity) {
                                getView().dismissLoading();
                                if (!TextUtils.isEmpty(copyEntity.getPath())) {
                                    try {
                                        if (!AppUtils.hasUpdate(copyEntity.getPath()))
                                            mPluginManager.startCMainActivity();
                                        else {
                                            mPluginManager.installCPlugin(copyEntity.getPath());
                                        }
                                    } catch (Exception e) {
                                        ToastUtils.show(Utils.appContext.getString(R.string.error_to_open_smart_home_activity));
                                    }
                                    return;
                                }

                                try {
                                    mPluginManager.startCMainActivity();
                                } catch (Exception e) {
                                    ToastUtils.show(Utils.appContext.getString(R.string.cannot_access_sd_card));
                                }

                            }
                        })
        );


    }
}
