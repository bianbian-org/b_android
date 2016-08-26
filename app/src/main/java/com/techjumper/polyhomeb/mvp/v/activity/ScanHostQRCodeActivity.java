package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.ScanHostQRCodeActivityPresenter;
import com.techjumper.zxing.ZXingScannerView;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(ScanHostQRCodeActivityPresenter.class)
public class ScanHostQRCodeActivity extends AppBaseActivity<ScanHostQRCodeActivityPresenter> {

    @Bind(R.id.zxing)
    ZXingScannerView mViewScanner;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activty_scan_host_qr_code);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.scan_qr_code);
    }

    public ZXingScannerView getScannerView() {
        return mViewScanner;
    }
}
