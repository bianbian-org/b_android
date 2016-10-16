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
                        .filter(aBoolean -> {
                            if (aBoolean != null && !aBoolean)
                                ToastUtils.show(Utils.appContext.getString(R.string.error_no_access_sd_card_permission));
                            return aBoolean != null && aBoolean;
                        })
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
                                ToastUtils.show(Utils.appContext.getString(R.string.cannot_access_sd_card));
                            }

                            @Override
                            public void onNext(PluginAssetsManager.CopyEntity copyEntity) {
                                if (!TextUtils.isEmpty(copyEntity.getPath())) {
                                    try {
                                        if (!AppUtils.hasUpdate(copyEntity.getPath())) {
                                            startCAppPlugin();
                                        } else {
                                            getView().dismissLoading();
                                            mPluginManager.installCPlugin(copyEntity.getPath());
                                        }
                                    } catch (Exception e) {
                                        ToastUtils.show(Utils.appContext.getString(R.string.error_to_open_smart_home_activity));
                                    }
                                    return;
                                }

                                try {
                                    startCAppPlugin();
                                } catch (Exception e) {
                                    ToastUtils.show(Utils.appContext.getString(R.string.cannot_access_sd_card));
                                }

                            }
                        })
        );


    }

    private void startCAppPlugin() {
        addSubscription(
                mPluginManager.startCMainActivityAuto()
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                JLog.d("打开页面失败：" + e);
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                getView().dismissLoading();
                                JLog.d(aBoolean != null && aBoolean ? "打开CApp成功" : "打开CApp失败");
                            }
                        })
        );
    }
}
