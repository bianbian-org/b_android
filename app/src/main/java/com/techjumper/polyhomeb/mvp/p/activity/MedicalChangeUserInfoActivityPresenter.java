package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.MedicalChangeUserInfoEvent;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalStatusEntity;
import com.techjumper.polyhomeb.mvp.m.MedicalChangeUserInfoActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangeUserInfoActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalLoginActivity;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import retrofit2.adapter.rxjava.Result;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class MedicalChangeUserInfoActivityPresenter extends AppBaseActivityPresenter<MedicalChangeUserInfoActivity> {

    private MedicalChangeUserInfoActivityModel mModel = new MedicalChangeUserInfoActivityModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public int getType() {
        return mModel.getType();
    }

    public String getTitle() {
        return mModel.getTitle();
    }

    public String getData() {
        return mModel.getData();
    }

    @OnClick(R.id.layout_clear_input)
    public void onClick(View view) {
        getView().getEtNickName().setText("");
    }

    public void onTitleRightClick() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.changeUserInfo(params())
                        .subscribe(new Observer<Result<MedicalStatusEntity>>() {
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
                            public void onNext(Result<MedicalStatusEntity> result) {
                                MedicalStatusEntity medicalStatusEntity = result.response().body();
                                if (449 == result.response().code()) {
                                    ToastUtils.show(getView().getString(R.string.medical_token_overdue));
                                    new AcHelper.Builder(getView()).closeCurrent(true).target(MedicalLoginActivity.class).start();
                                }
                                if (medicalStatusEntity == null) return;
                                int status = medicalStatusEntity.getStatus();

                                switch (status) {
                                    case 1:
                                        ToastUtils.show(getView().getString(R.string.medical_change_success));
                                        UserManager.INSTANCE.saveUserInfo(mModel.getKey(), getView().getEtNickName().getEditableText().toString());
                                        RxBus.INSTANCE.send(new MedicalChangeUserInfoEvent(mModel.getType(), getView().getEtNickName().getEditableText().toString(), mModel.getPosition()));
                                        getView().finish();
                                        break;
                                    case 2:
                                        ToastUtils.show(getView().getString(R.string.medical_user_name_exist));
                                        break;
//                                    case 3:
//                                        ToastUtils.show(getView().getString(R.string.medical_email_exist));
//                                        break;
//                                    case 4:
//                                        ToastUtils.show(getView().getString(R.string.medical_phone_number_exist));
//                                        break;
                                    case 5:
                                        ToastUtils.show(getView().getString(R.string.medical_id_card_exist));
                                        break;
                                    case 6:
                                        ToastUtils.show(getView().getString(R.string.medical_user_name_partten_wrong));
                                        break;
//                                    case 7:
//                                        ToastUtils.show(getView().getString(R.string.medical_email_wrong));
//                                        break;
//                                    case 8:
//                                        ToastUtils.show(getView().getString(R.string.medical_phone_wrong));
//                                        break;
                                    case 9:
                                        ToastUtils.show(getView().getString(R.string.medical_id_card_wrong));
                                        break;
//                                    case 10:
//                                        ToastUtils.show(getView().getString(R.string.medical_email_code_wrong));
//                                        break;
//                                    case 11:
//                                        ToastUtils.show(getView().getString(R.string.medical_phone_code_wrong));
//                                        break;
//                                    case 12:
//                                        ToastUtils.show(getView().getString(R.string.medical_email_wrong__));
//                                        break;
//                                    case 13:
//                                        ToastUtils.show(getView().getString(R.string.medical_phone_wrong__));
//                                        break;
//                                    case 17:
//                                        ToastUtils.show(getView().getString(R.string.medical_email_wrong___));
//                                        break;
//                                    case 18:
//                                        ToastUtils.show(getView().getString(R.string.medical_phone_wrong___));
//                                        break;
                                    case -1:
                                        ToastUtils.show(getView().getString(R.string.medical_unknow_error));
                                        break;
                                }
                            }
                        })
        );
    }

    private Map<String, String> params() {
        Map<String, String> map = new HashMap<>();
        map.put(mModel.getParamName(), getView().getEtNickName().getEditableText().toString());
        return map;
    }

}
