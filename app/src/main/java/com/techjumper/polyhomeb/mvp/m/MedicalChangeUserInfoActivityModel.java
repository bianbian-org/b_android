package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalStatusEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangeUserInfoActivityPresenter;
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

public class MedicalChangeUserInfoActivityModel extends BaseModel<MedicalChangeUserInfoActivityPresenter> {

    public MedicalChangeUserInfoActivityModel(MedicalChangeUserInfoActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public int getType() {
        return getExtra().getInt(MedicalUserInfoActivityPresenter.TYPE, 0);
    }

    public String getData() {
        return getExtra().getString(MedicalUserInfoActivityPresenter.DATA, "");
    }

    public int getPosition() {
        return getExtra().getInt(MedicalUserInfoActivityPresenter.POSITION, 0);
    }

    public String getTitle() {
        switch (getType()) {
            case MedicalUserInfoActivityPresenter.NICKNAME:
                return getPresenter().getView().getString(R.string.medical_change_nick_name);
            case MedicalUserInfoActivityPresenter.NAME:
                return getPresenter().getView().getString(R.string.medical_change_p_name);
            case MedicalUserInfoActivityPresenter.IDCARD:
                return getPresenter().getView().getString(R.string.medical_change_id_card);
            case MedicalUserInfoActivityPresenter.HOMEPHONE:
                return getPresenter().getView().getString(R.string.medical_change_home_phone);
//            case MedicalUserInfoActivityPresenter.MOBILEPHONE:
//                return getPresenter().getView().getString(R.string.medical_change_mobile_phone);
//            case MedicalUserInfoActivityPresenter.EMAIL:
//                return getPresenter().getView().getString(R.string.medical_change_email);
        }
        return "";
    }

    public String getParamName() {
        switch (getType()) {
            case MedicalUserInfoActivityPresenter.NICKNAME:
                return "nickname";
            case MedicalUserInfoActivityPresenter.NAME:
                return "pname";
            case MedicalUserInfoActivityPresenter.IDCARD:
                return "idcard";
            case MedicalUserInfoActivityPresenter.HOMEPHONE:
                return "homephone";
//            case MedicalUserInfoActivityPresenter.MOBILEPHONE:
//                return "mobilephone";
//            case MedicalUserInfoActivityPresenter.EMAIL:
//                return "email";
        }
        return "";
    }

    public String getKey() {
        switch (getType()) {
            case MedicalUserInfoActivityPresenter.NICKNAME:
                return UserManager.KEY_MEDICAL_CURRENT_USER_NICK_NAME;
            case MedicalUserInfoActivityPresenter.NAME:
                return UserManager.KEY_MEDICAL_CURRENT_USER_P_NAME;
            case MedicalUserInfoActivityPresenter.IDCARD:
                return UserManager.KEY_MEDICAL_CURRENT_USER_ID_CARD;
            case MedicalUserInfoActivityPresenter.HOMEPHONE:
                return UserManager.KEY_MEDICAL_CURRENT_USER_HOME_PHONE;
//            case MedicalUserInfoActivityPresenter.MOBILEPHONE:
//                return UserManager.KEY_MEDICAL_CURRENT_USER_MOBILE_PHONE;
//            case MedicalUserInfoActivityPresenter.EMAIL:
//                return UserManager.KEY_MEDICAL_CURRENT_USER_EMAIL;
        }
        return "";
    }

    public Observable<Result<MedicalStatusEntity>> changeUserInfo(Map<String, String> map) {
        return RetrofitHelper.<ServiceAPI>createMedicalConnection()
                .changeUserInfo(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_TOKEN)
                        , map)
                .compose(CommonWrap.wrap());
    }


}
