package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalStatusEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalVerificationCodeEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangUserEmailActivityPresenter;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalUserInfoActivityPresenter;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.Map;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/9/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class MedicalChangUserEmailActivityModel extends BaseModel<MedicalChangUserEmailActivityPresenter> {

    public MedicalChangUserEmailActivityModel(MedicalChangUserEmailActivityPresenter presenter) {
        super(presenter);
    }

    //下发验证码
    public Observable<Result<MedicalVerificationCodeEntity>> getVerificationCode(Map<String, String> map) {
        return RetrofitHelper
                .<ServiceAPI>createMedicalConnection()
                .getVerificationCode(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_TOKEN)
                        , map)
                .compose(CommonWrap.wrap());
    }

    //修改邮箱
    public Observable<Result<MedicalStatusEntity>> changeUserEmailInfo(String cookie, Map<String, String> map) {
        return RetrofitHelper
                .<ServiceAPI>createMedicalConnection()
                .changeUserEmailInfo(cookie
                        , UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_TOKEN)
                        , map)
                .compose(CommonWrap.wrap());
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    //类型是邮箱
    public int getType() {
        return getExtra().getInt(MedicalUserInfoActivityPresenter.TYPE, 0);
    }

    //具体邮箱地址
    public String getData() {
        return getExtra().getString(MedicalUserInfoActivityPresenter.DATA, "");
    }

    public int getPosition() {
        return getExtra().getInt(MedicalUserInfoActivityPresenter.POSITION, 0);
    }

}
