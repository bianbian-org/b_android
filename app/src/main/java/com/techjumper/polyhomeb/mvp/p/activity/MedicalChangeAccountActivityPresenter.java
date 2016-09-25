package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.MedicalChangeAccountEvent;
import com.techjumper.polyhomeb.entity.event.ReloadMedicalMainEvent;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalAllUserEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserLoginEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalChangeAccountActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeAccountActivity;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.Result;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalChangeAccountActivityPresenter extends AppBaseActivityPresenter<MedicalChangeAccountActivity> {

    private MedicalChangeAccountActivityModel mModel = new MedicalChangeAccountActivityModel(this);

    private Subscription mSubs1, mSubs2, mSubs3;

    private MedicalAllUserEntity mSpEntity;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getSpDatas();
        onItemClick();
    }

    private void onItemClick() {
        addSubscription(
                mSubs2 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(o -> {
                            if (o instanceof MedicalChangeAccountEvent) {
                                MedicalChangeAccountEvent event = (MedicalChangeAccountEvent) o;
                                String name = event.getName();
                                List<MedicalAllUserEntity> medicalAllUserInfo = UserManager.INSTANCE.getMedicalAllUserInfo();
                                for (int i = 0; i < medicalAllUserInfo.size(); i++) {
                                    MedicalAllUserEntity medicalAllUserEntity = medicalAllUserInfo.get(i);
                                    if (medicalAllUserEntity.getUsername().endsWith(name)) {
                                        mSpEntity = medicalAllUserEntity;
                                        changeAccount();
                                        break;
                                    }
                                }
                            }
                        })
        );
    }

    private void getSpDatas() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getSpData()
                        .subscribe(new Subscriber<List<DisplayBean>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<DisplayBean> dataList) {
                                getView().onDataReceived(dataList);
                            }
                        })
        );
    }

    private void changeAccount() {
        if (mSpEntity == null ||
                TextUtils.isEmpty(mSpEntity.getUsername())
                || TextUtils.isEmpty(mSpEntity.getPassword())) return;
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = mModel.login(mSpEntity.getUsername(), mSpEntity.getPassword())
                        .subscribe(new Observer<Result<MedicalUserLoginEntity>>() {
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
                            public void onNext(Result<MedicalUserLoginEntity> result) {
                                if (result == null
                                        || result.response().body() == null
                                        || result.response().body().getStatus() != 1) {
                                    ToastUtils.show(getView().getString(R.string.medical_unknow_error));
                                    return;
                                }
                                if (401 == result.response().code()) {
                                    ToastUtils.show(getView().getString(R.string.medical_userid_pasw_wrong));
                                    return;
                                }
                                String nztoken = result.response().headers().get("nztoken");
                                if (TextUtils.isEmpty(nztoken)) return;
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_TOKEN, nztoken);
                                UserManager.INSTANCE.saveUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_ID
                                        , mSpEntity.getUsername());
                                UserManager.INSTANCE.saveMedicalUserInfo(result.response().body());
                                RxBus.INSTANCE.send(new ReloadMedicalMainEvent());
                                saveAllInfo2Sp(nztoken, result.response().body());
                                ToastUtils.show(getView().getString(R.string.medical_change_account_success));
                                notifyRvItemChanged();
                            }
                        })
        );
    }

    //将用户信息存入SP的List.
    private void saveAllInfo2Sp(String token, MedicalUserLoginEntity medicalUserLoginEntity) {

        List<MedicalAllUserEntity> userInfo = UserManager.INSTANCE.getMedicalAllUserInfo();

        boolean hasUserInfo = false;

        //如果不存在集合,就新建一个
        if (userInfo == null) {
            userInfo = new ArrayList<>();
        }

        //遍历集合,如果集合中已经有当前登录的信息了,就true,否则false
        for (MedicalAllUserEntity entity : userInfo) {
            if (entity.getId().equals(medicalUserLoginEntity.getMember().getId())) {
                hasUserInfo = true;
                break;
            }
        }

        //如果集合中有当前登录的信息了,就修改这些信息为最新的
        if (hasUserInfo) {
            for (MedicalAllUserEntity entity : userInfo) {
                if (entity.getId().equals(medicalUserLoginEntity.getMember().getId())) {
                    entity.setpName(medicalUserLoginEntity.getMember().getPname());
                    entity.setPassword(mSpEntity.getPassword());
                    entity.setToken(token);
                    entity.setNickName(medicalUserLoginEntity.getMember().getNickname());
                    entity.setUsername(mSpEntity.getUsername());
                    break;
                }
            }
            //如果没有,就将这些信息add进集合
        } else {
            MedicalAllUserEntity medicalAllUserEntity = new MedicalAllUserEntity();
            medicalAllUserEntity.setPassword(mSpEntity.getPassword());
            medicalAllUserEntity.setId(medicalUserLoginEntity.getMember().getId());
            medicalAllUserEntity.setNickName(medicalUserLoginEntity.getMember().getNickname());
            medicalAllUserEntity.setpName(medicalUserLoginEntity.getMember().getPname());
            medicalAllUserEntity.setToken(token);
            medicalAllUserEntity.setUsername(mSpEntity.getUsername());
            userInfo.add(medicalAllUserEntity);
        }
        //最后将集合存入SP
        UserManager.INSTANCE.saveMedicalAllUserInfo(userInfo);
    }

    private void notifyRvItemChanged() {
        getSpDatas();
    }


}
