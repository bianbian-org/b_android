package com.techjumper.polyhomeb.mvp.v.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.SettingActivityPresenter;

import java.io.File;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(SettingActivityPresenter.class)
public class SettingActivity extends AppBaseActivity<SettingActivityPresenter> {

    @Bind(R.id.tv_current_version)
    TextView mTvVersionInfo;   //版本信息
    @Bind(R.id.tv_clear_cache)
    TextView mTvClearCache;   //清除缓存
    @Bind(R.id.tv_appraise)
    TextView mTvAppraise;    //评价
    @Bind(R.id.tv_about)
    TextView mTvAbout;       //关于
    @Bind(R.id.tv_cache_size)
    TextView mTvCache;       //缓存大小
    @Bind(R.id.tv_uninstall_smarthome)
    TextView mTvUninstallSmarthome;
    @Bind(R.id.smart_home_divider)
    View mSmartHomeDivider;
    @Bind(R.id.tv_logout)
    TextView mTvLogout;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_setting);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        showCacheSize();
        showAppCurrentVersion();
        mTvAbout.setOnLongClickListener(v -> {
            new AcHelper.Builder(this)
                    .target(DebugActivity.class)
                    .start();
            return true;
        });

        initPluginLayout();

    }

    private void showAppCurrentVersion() {
        String version_name = "";
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version_name += info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTvVersionInfo.setText(String.format(getString(R.string.current_version), version_name));
    }

    private void initPluginLayout() {
        boolean installCPlugin = getPresenter().getPluginManager().isInstallCPlugin();
        int visibility = installCPlugin ? View.VISIBLE : View.GONE;
        mTvUninstallSmarthome.setVisibility(visibility);
        mSmartHomeDivider.setVisibility(visibility);

        int margin = installCPlugin ? 0 : getResources().getDimensionPixelSize(R.dimen.dp_54);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mTvLogout.getLayoutParams();
        layoutParams.topMargin = margin;
        mTvLogout.setLayoutParams(layoutParams);
    }

    @Override
    public void onResume() {
        super.onResume();
        initPluginLayout();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.settings);
    }

    private void showCacheSize() {
        long size = 0;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Config.sParentDirName;
        try {
            size = FileUtils.getFolderSize(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String stringSize = FileUtils.formatFileSize(size);
        mTvCache.setText(stringSize);
    }

    public TextView getTvCache() {
        return mTvCache;
    }

}
