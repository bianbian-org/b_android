package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.google.zxing.Result;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.system.VibrateUtil;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.m.ScanHostQRCodeActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ScanHostQRCodeActivity;
import com.techjumper.polyhomeb.utils.SoundUtils;
import com.techjumper.zxing.ZXingScannerView;

import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ScanHostQRCodeActivityPresenter extends AppBaseActivityPresenter<ScanHostQRCodeActivity>
        implements ZXingScannerView.ResultHandler {

    private ScanHostQRCodeActivityModel mModel = new ScanHostQRCodeActivityModel(this);

    private Subscription mSubs1, mSubs2;

    public static final String KEY_QR_CODE = "key_qr_code";

    @Override
    public void initData(Bundle savedInstanceState) {

    }


    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxPermissions.getInstance(getView())
                        .request(Manifest.permission.CAMERA)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                getView().getScannerView().setResultHandler(this);
                                getView().getScannerView().startCamera();
                                getView().getScannerView().setAutoFocus(true);
                            } else {
                                FragmentActivity ac = getView();
                                if (ac != null && !ac.isFinishing()) {
                                    AcHelper.finish(ac);
                                }
                                getView().showHint(Utils.appContext.getString(R.string.error_open_camera));
                            }
                        })
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        getView().getScannerView().stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        VibrateUtil.vibrate(40);
        SoundUtils.playScanSound();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_QR_CODE, rawResult.getText());
        ToastUtils.show(rawResult.getText());
//        bundle.putString(KEY_QR_CODE, "TWHB0001161266900002");
//        new AcHelper.Builder(getView())
//                .extra(bundle)
//                .target(AddCameraActivity.class)
//                .closeCurrent(true)
//                .start();
        uploadData(rawResult.getText());

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (getView() == null || getView().getScannerView() == null || getView() == null
                    || getView().isFinishing()) return;
            getView().getScannerView().resumeCameraPreview(ScanHostQRCodeActivityPresenter.this);
        }, 2000);
    }

    private void uploadData(String result) {
        RxUtils.unsubscribeIfNotNull(mSubs2);
//        addSubscription(
//                mSubs2 = mModel.uploadData(result));
    }
}
