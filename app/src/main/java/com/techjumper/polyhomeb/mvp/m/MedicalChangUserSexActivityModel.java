package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalStatusEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangUserSexActivityPresenter;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalUserInfoActivityPresenter;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.Map;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class MedicalChangUserSexActivityModel extends BaseModel<MedicalChangUserSexActivityPresenter> {

    public MedicalChangUserSexActivityModel(MedicalChangUserSexActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<Result<MedicalStatusEntity>> changeUserInfo(Map<String, Integer> map) {
        return RetrofitHelper.<ServiceAPI>createMedicalConnection()
                .changeUserSexInfo(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_TOKEN)
                        , map)
                .compose(CommonWrap.wrap());
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public int getType() {
        return getExtra().getInt(MedicalUserInfoActivityPresenter.TYPE, 0);
    }

    public String getData() {
        return getExtra().getString(MedicalUserInfoActivityPresenter.DATA, "");  //"1"  "2"  ""
    }

    public int getPosition() {
        return getExtra().getInt(MedicalUserInfoActivityPresenter.POSITION, 0);
    }

}
