package com.techjumper.polyhomeb.mvp.p.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.UpdateInfoEntity;
import com.techjumper.polyhomeb.manager.PolyPluginManager;
import com.techjumper.polyhomeb.mvp.m.SettingActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.AboutUsActivity;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.SettingActivity;
import com.techjumper.polyhomeb.service.UpdateService;
import com.techjumper.polyhomeb.user.UserManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

import static com.techjumper.polyhomeb.service.UpdateService.KEY_URL;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SettingActivityPresenter extends AppBaseActivityPresenter<SettingActivity> {

    private SettingActivityModel mModel = new SettingActivityModel(this);

    private PolyPluginManager mPluginManager;
    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {
        mPluginManager = PolyPluginManager.with(getView());
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_logout, R.id.layout_cache, R.id.tv_uninstall_smarthome, R.id.tv_about, R.id.layout_version_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_version_info:
                checkVersionInfo();
                break;
            case R.id.tv_logout:
                logout();
                break;
            case R.id.layout_cache:
                clearCache();
                break;
            case R.id.tv_uninstall_smarthome:
                mPluginManager.uninstallCPlugin();
                break;
            case R.id.tv_about:
                aboutUs();
                break;
        }
    }

    private void logout() {
        DialogUtils.getBuilder(getView())
                .content(R.string.confirm_logout)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            UserManager.INSTANCE.logout();
                            new AcHelper.Builder(getView())
                                    .target(LoginActivity.class)
                                    .closeCurrent(true)
                                    .start();
                            break;
                    }
                })
                .show();
    }

    private void clearCache() {
        DialogUtils.getBuilder(getView())
                .content(R.string.confirm_clear_cache)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Config.sParentDirName;
                            try {
                                FileUtils.deleteFolderFile(path, false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            getView().getTvCache().setText("0M");
                            break;
                    }
                })
                .show();
    }

    public PolyPluginManager getPluginManager() {
        return mPluginManager;
    }

    private void aboutUs() {
        new AcHelper.Builder(getView()).target(AboutUsActivity.class).start();
    }

    private void checkVersionInfo() {
        getView().showLoading();
        String pkgName = getView().getPackageName();
        String[] name = new String[]{pkgName};
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.checkUpdate(name)
                        .subscribe(new Observer<UpdateInfoEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                            }

                            @Override
                            public void onNext(UpdateInfoEntity updateInfoEntity) {
                                if (!processNetworkResult(updateInfoEntity)) return;
                                if (updateInfoEntity.getData() != null
                                        && updateInfoEntity.getData().getResult() != null
                                        && updateInfoEntity.getData().getResult().size() != 0) {
                                    List<UpdateInfoEntity.DataBean.ResultBean> result = updateInfoEntity.getData().getResult();
                                    mModel.analyzeVersionCode(result);
                                } else {
                                    ToastUtils.show(getView().getString(R.string.no_new_version));
                                }
                            }
                        }));
    }

    public void showDialog(String url) {
        DialogUtils.getBuilder(getView())
                .content(R.string.has_new_version)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            downloadApk(url);
                            break;
                    }
                })
                .show();
    }

    private void downloadApk(String url) {
        Intent intent = new Intent(getView(), UpdateService.class);
        intent.putExtra(KEY_URL, Config.sHost + url);
        getView().startService(intent);

    }


}
