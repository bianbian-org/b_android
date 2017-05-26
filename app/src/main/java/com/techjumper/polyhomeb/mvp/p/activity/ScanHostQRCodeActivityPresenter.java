package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;

import com.google.zxing.Result;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.system.VibrateUtil;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.JoinFamilyEntity;
import com.techjumper.polyhomeb.entity.QueryFamilyEntity;
import com.techjumper.polyhomeb.entity.VillageLockEntity;
import com.techjumper.polyhomeb.entity.event.BLEInfoChangedEvent;
import com.techjumper.polyhomeb.mvp.m.ScanHostQRCodeActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ScanHostQRCodeActivity;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.utils.SoundUtils;
import com.techjumper.zxing.ZXingScannerView;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ScanHostQRCodeActivityPresenter extends AppBaseActivityPresenter<ScanHostQRCodeActivity>
        implements ZXingScannerView.ResultHandler {

    private ScanHostQRCodeActivityModel mModel = new ScanHostQRCodeActivityModel(this);

    private Subscription mSubs1, mSubs2, mSubs3;

    private static final String KEY = "jumper_polyhome_b";

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
        getView().showLoading();
        VibrateUtil.vibrate(40);
        SoundUtils.playScanSound();
        joinFamily(rawResult.getText());
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (getView() == null || getView().getScannerView() == null || getView() == null
                    || getView().isFinishing()) return;
            getView().getScannerView().resumeCameraPreview(ScanHostQRCodeActivityPresenter.this);
        }, 2000);
    }

    private void joinFamily(String result) {
        String decrypt = decrypt(result);
        if (TextUtils.isEmpty(decrypt)) {
            ToastUtils.show(getView().getString(R.string.scan_qr_code_error));
            getView().dismissLoading();
            return;
        }
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.joinFamily(decrypt)
                        .flatMap(joinFamilyEntity -> {
                            if (joinFamilyEntity.getError_code() == 109) {
                                return Observable.error(new Exception(getView().getString(R.string.not_login)));
                            }
                            if (joinFamilyEntity.getError_code() == 302) {
                                return Observable.error(new Exception(getView().getString(R.string.join_family_error)));
                            }
                            JoinFamilyEntity.DataBean data = joinFamilyEntity.getData();
                            int family_id = data.getFamily_id();
                            String family_name = data.getFamily_name();
                            int village_id = data.getVillage_id();
                            UserManager.INSTANCE.updateFamilyOrVillageInfo(true, family_id + ""
                                    , family_name, village_id + "");
                            return mModel.getBLEDoorInfo(village_id + "");
                        }).flatMap(new Func1<BluetoothLockDoorInfoEntity, Observable<QueryFamilyEntity>>() {
                            @Override
                            public Observable<QueryFamilyEntity> call(BluetoothLockDoorInfoEntity bluetoothLockDoorInfoEntity) {
                                if (!processNetworkResult(bluetoothLockDoorInfoEntity)) {
                                    return Observable.error(new Exception(getView().getString(R.string.error_data)));
                                }
                                if (bluetoothLockDoorInfoEntity != null
                                        && bluetoothLockDoorInfoEntity.getData() != null) {
                                    ToastUtils.show(getView().getString(R.string.join_family_success));
                                    //切换家庭或者小区之后，发送消息给HomeFragment,刷新首页数据
                                    UserManager.INSTANCE.saveBLEInfo(bluetoothLockDoorInfoEntity);
                                    RxBus.INSTANCE.send(new BLEInfoChangedEvent());
                                    if (!UserManager.INSTANCE.isCurrentCommunitySupportBLEDoor()) {
                                        getDnakeInfo();
                                    }
                                } else {
                                    return Observable.error(new Exception(getView().getString(R.string.error_data)));
                                }
                                //这里不需要进行判断了，因为这里本身就是扫码加入家庭的，并不是小区
//                                if (UserManager.INSTANCE.isFamily()) {
//                                    return mModel.getCurrentFamilyAdminUserId();
//                                } else {
//                                    return Observable.empty();
//                                }
                                return mModel.getCurrentFamilyAdminUserId();
                            }
                        }).subscribe(new Observer<QueryFamilyEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
//                                getView().showHint(e.toString());
                                getView().showHint(e.getMessage().toString());
                            }

                            @Override
                            public void onNext(QueryFamilyEntity queryFamilyEntity) {
                                if (!processNetworkResult(queryFamilyEntity)) return;
                                if (queryFamilyEntity != null && queryFamilyEntity.getData() != null) {
                                    QueryFamilyEntity.DataEntity data = queryFamilyEntity.getData();
                                    String manager_id = data.getManager_id();
                                    UserManager.INSTANCE.saveUserInfo(UserManager.KEY_CURRENT_FAMILY_MANAGER_ID, manager_id);
                                    jumpToTabHomeActivity();
                                } else {
                                    getView().showHint(getView().getString(R.string.error_data));
                                }
                            }
                        }));
    }

    private String decrypt(String encryptString) {
        if (TextUtils.isEmpty(encryptString))
            return "";

        String result = new String(Base64.decode(encryptString, Base64.DEFAULT));
        if (!result.contains(":")) {
            return "";
        }
        if (result.length() <= 1) {
            return "";
        }
        String key = result.substring(0, result.indexOf(":"));

        if (TextUtils.isEmpty(key) || !key.equals(KEY))
            return "";

        return result.substring(result.indexOf(":") + 1, result.length());
    }

    private void jumpToTabHomeActivity() {
        new AcHelper.Builder(getView())
                .target(TabHomeActivity.class)
                .closeCurrent(true)
                .start();
    }

    private void getDnakeInfo() {
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = mModel.getVillageLocks()
                        .subscribe(new Observer<VillageLockEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(VillageLockEntity villageLockEntity) {
                                if (!processNetworkResult(villageLockEntity)) return;
                                UserManager.INSTANCE.saveDnakeInfo(villageLockEntity);
                                RxBus.INSTANCE.send(new BLEInfoChangedEvent());
                            }
                        }));
    }

}
