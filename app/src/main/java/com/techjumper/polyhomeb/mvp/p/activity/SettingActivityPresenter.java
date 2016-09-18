package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.manager.PolyPluginManager;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.SettingActivity;
import com.techjumper.polyhomeb.user.UserManager;

import java.io.File;
import java.io.IOException;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SettingActivityPresenter extends AppBaseActivityPresenter<SettingActivity> {

    private PolyPluginManager mPluginManager;

    @Override
    public void initData(Bundle savedInstanceState) {
        mPluginManager = PolyPluginManager.with(getView());
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_logout, R.id.layout_cache, R.id.tv_uninstall_smarthome})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_logout:
                logout();
                break;
            case R.id.layout_cache:
                clearCache();
                break;
            case R.id.tv_uninstall_smarthome:
                mPluginManager.uninstallCPlugin();
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
}
