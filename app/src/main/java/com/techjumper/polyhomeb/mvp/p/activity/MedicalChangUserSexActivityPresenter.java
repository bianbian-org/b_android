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
import com.techjumper.polyhomeb.mvp.m.MedicalChangUserSexActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalChangUserSexActivity;
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

public class MedicalChangUserSexActivityPresenter extends AppBaseActivityPresenter<MedicalChangUserSexActivity> {

    private MedicalChangUserSexActivityModel mModel = new MedicalChangUserSexActivityModel(this);

    public String mSex = "";
    public int mSex_ = 0;
    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick({R.id.layout_male, R.id.layout_female})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_male:
                mSex = getView().getString(R.string.male);
                mSex_ = 1;
                getView().getIvMale().setVisibility(View.VISIBLE);
                getView().getIvFemale().setVisibility(View.GONE);
                break;
            case R.id.layout_female:
                mSex = getView().getString(R.string.female);
                mSex_ = 2;
                getView().getIvMale().setVisibility(View.GONE);
                getView().getIvFemale().setVisibility(View.VISIBLE);
                break;
        }
    }

    public String getSex() {  //"1",,"2".....""
        mSex = mModel.getData();
        return mModel.getData();
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
                                        UserManager.INSTANCE.saveUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_SEX, mSex_ + "");
                                        RxBus.INSTANCE.send(new MedicalChangeUserInfoEvent(mModel.getType(), mSex_ + "" + "", mModel.getPosition()));
                                        getView().finish();
                                        break;
                                    case -1:
                                        ToastUtils.show(getView().getString(R.string.medical_unknow_error));
                                        break;
                                }
                            }
                        })
        );
    }

    private Map<String, Integer> params() {
        Map<String, Integer> map = new HashMap<>();
        map.put("sex", mSex_);
        return map;
    }
}
