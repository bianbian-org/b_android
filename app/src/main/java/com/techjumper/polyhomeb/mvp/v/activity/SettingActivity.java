package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
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

    @Bind(R.id.tv_clear_cache)
    TextView mTvClearCache;   //清除缓存
    @Bind(R.id.tv_appraise)
    TextView mTvAppraise;    //评价
    @Bind(R.id.tv_about)
    TextView mTvAbout;       //关于
    @Bind(R.id.tv_cache_size)
    TextView mTvCache;       //缓存大小

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_setting);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        showCacheSize();
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
