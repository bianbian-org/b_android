package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.basic.StringUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.entity.event.BLEInfoChangedEvent;
import com.techjumper.polyhomeb.entity.event.ChangeVillageIdRefreshEvent;
import com.techjumper.polyhomeb.mvp.m.LoginActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ChooseVillageFamilyActivity;
import com.techjumper.polyhomeb.mvp.v.activity.FindPasswordActivity;
import com.techjumper.polyhomeb.mvp.v.activity.LoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.RegistActivity;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.user.event.LoginEvent;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class LoginActivityPresenter extends AppBaseActivityPresenter<LoginActivity> {

    public static final String KEY_PHONE_NUMBER = "key_phone_number";
    public static final String KEY_COME_FROM = "key_come_from";
    public static final String VALUE_COME_FROM_WEBVIEW = "key_come_from_webview";

    private Subscription mSubs1,mSubs2;
    private String mPhoneNumber;

    private LoginActivityModel mModel = new LoginActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    public void onTitleLeftClick() {
        if (getView().isShowLeft()) {
            getView().onBackPressed();
        }
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        //监听注册成功的消息
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE.asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if (o instanceof LoginEvent) {
                                LoginEvent event = (LoginEvent) o;
                                if (event.isLogin()) {
                                    getView().dismissLoading();
                                    if (UserManager.INSTANCE.hasChoosedFamilyOrVillage()) {
                                        if (VALUE_COME_FROM_WEBVIEW.equals(mModel.getComeFrom())) {
                                            //发出消息,让webview们重新加载,带上header
                                            RxBus.INSTANCE.send(new ChangeVillageIdRefreshEvent());
                                            getView().finish();
                                        } else {
                                            new AcHelper.Builder(getView())
                                                    .target(TabHomeActivity.class)
                                                    .closeCurrent(true)
                                                    .enterAnim(R.anim.fade_in)
                                                    .exitAnim(R.anim.fade_out)
                                                    .start();
                                        }
                                    } else {
                                        new AcHelper.Builder(getView())
                                                .target(ChooseVillageFamilyActivity.class)
                                                .closeCurrent(true)
                                                .start();
                                    }
                                }
                            }
                        })
        );
    }

    @OnClick({R.id.tv_login, R.id.tv_forget_psw, R.id.tv_regist})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PHONE_NUMBER, getView().getPhoneNumber());
        switch (view.getId()) {
            case R.id.tv_login:
                checkingInputAndLogin();
                break;
            case R.id.tv_forget_psw:
                new AcHelper.Builder(getView()).extra(bundle).target(FindPasswordActivity.class).start();
                break;
            case R.id.tv_regist:
                new AcHelper.Builder(getView()).extra(bundle).target(RegistActivity.class).start();
                break;
        }
    }


    private void checkingInputAndLogin() {
        EditText et = null;
        mPhoneNumber = getView().getPhoneNumber();
        if (!StringUtils.PATTERN_MOBILE.matcher(mPhoneNumber).matches()) {
            et = getView().getEtAccount();
            getView().setText(et, et.getText());
            getView().getLayoutWrong().setVisibility(View.VISIBLE);
        } else {
            getView().getLayoutWrong().setVisibility(View.INVISIBLE);
        }

        if (!StringUtils.PATTERN_PASSWORD.matcher(getView().getEtPsw().getText().toString()).matches()) {
            et = getView().getEtPsw();
            getView().setText(et, et.getText());
            getView().getLayoutWrong().setVisibility(View.VISIBLE);
        } else {
            getView().getLayoutWrong().setVisibility(View.INVISIBLE);
        }
        if (et != null) {
            getView().showKeyboard(et);
        } else {
            login();
        }
    }

    private void login() {
        getView().showLoading();
        addSubscription(
                mModel.login(getView().getPhoneNumber()
                        , getView().getEtPsw().getText().toString())
                        .subscribe(new Subscriber<LoginEntity>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                getView().dismissLoading();
                            }

                            @Override
                            public void onNext(LoginEntity entity) {
                                if (!processNetworkResult(entity)) {
                                    getView().dismissLoading();
                                    getView().clearPassword();
                                    return;
                                }

                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_PHONE_NUMBER, mPhoneNumber);
                                UserManager.INSTANCE.saveUserInfo(entity);
                                getView().showHint(getView().getString(R.string.success_login));
//                                UserManager.INSTANCE.notifyLoginOrLogoutEvent(true);
                                getBLEDoorInfo();
                            }

                        })
        );
    }

    public String getComeFrom() {
        return mModel.getComeFrom();
    }

    private void getBLEDoorInfo() {
        boolean isFamily = UserManager.INSTANCE.isFamily();//true为家庭,false为小区
        String family_id = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID);
        String village_id = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID);
        if (!isFamily) {
            family_id = "";
        }
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.getBLEDoorInfo(village_id, family_id)
                        .subscribe(new Observer<BluetoothLockDoorInfoEntity>() {
                            @Override
                            public void onCompleted() {
//                                getView().dismissLoading();
                                UserManager.INSTANCE.notifyLoginOrLogoutEvent(true);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
//                                getView().showError(e);
                            }

                            @Override
                            public void onNext(BluetoothLockDoorInfoEntity bluetoothLockDoorInfoEntity) {
                                if (!processNetworkResult(bluetoothLockDoorInfoEntity)) return;
                                if (bluetoothLockDoorInfoEntity != null
                                        && bluetoothLockDoorInfoEntity.getData() != null) {
                                    //切换家庭或者小区之后，发送消息给HomeFragment,刷新首页数据
                                    RxBus.INSTANCE.send(new BLEInfoChangedEvent());
                                    UserManager.INSTANCE.saveBLEInfo(bluetoothLockDoorInfoEntity);
                                }
                            }
                        }));
    }
}
