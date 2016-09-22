package com.techjumper.polyhomeb.mvp.m;

import android.text.TextUtils;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalUserInfoBlackData;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalUserInfoData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MedicalUserInfoBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MedicalUserInfoBlackBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserInfoEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalStatusEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalUserInfoActivityPresenter;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Subscriber;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalUserInfoActivityModel extends BaseModel<MedicalUserInfoActivityPresenter> {

    public MedicalUserInfoActivityModel(MedicalUserInfoActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<List<DisplayBean>> getInfo() {
        return Observable
                .create(new Observable.OnSubscribe<List<DisplayBean>>() {
                    @Override
                    public void call(Subscriber<? super List<DisplayBean>> subscriber) {
                        MedicalUserInfoEntity entity = new MedicalUserInfoEntity();
                        MedicalUserInfoEntity.DataBean data = new MedicalUserInfoEntity.DataBean();
                        data.setBirthday(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_BIRTHDAY));
                        data.setEmail(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_EMAIL));
                        data.setHeight(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_HEIGHT));
                        data.setHomenumber(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_HOME_PHONE));
                        data.setIdcard(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_ID_CARD));
                        data.setName(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_P_NAME));
                        data.setNickName(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_NICK_NAME));
                        data.setPhone(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_MOBILE_PHONE));
                        data.setSex(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_SEX));
                        data.setWeight(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_WEIGHT));
                        entity.setData(data);
                        subscriber.onNext(getData(entity));
                        subscriber.onCompleted();
                    }
                })
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> getData(MedicalUserInfoEntity entity) {
        List<DisplayBean> displayBeen = new ArrayList<>();

        //空白布局
        MedicalUserInfoBlackData medicalUserInfoBlackData = new MedicalUserInfoBlackData();
        MedicalUserInfoBlackBean medicalUserInfoBlackBean = new MedicalUserInfoBlackBean(medicalUserInfoBlackData);
        displayBeen.add(medicalUserInfoBlackBean);

        //细长分割线
        PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
        PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
        displayBeen.add(propertyPlacardDividerLongBean);

        //短分割线 先初始化出来,不用
        PropertyPlacardDividerData propertyPlacardDividerData = new PropertyPlacardDividerData();
        propertyPlacardDividerData.setMarginLeft(RuleUtils.dp2Px(30));//和布局中文字的marginLeft相同
        PropertyPlacardDividerBean propertyPlacardDividerBean = new PropertyPlacardDividerBean(propertyPlacardDividerData);

        //前面的五个布局
        for (int i = 1; i <= 5; i++) {
            MedicalUserInfoData medicalUserInfoData = new MedicalUserInfoData();
            medicalUserInfoData.setLabel(getLabelByPosition(i));
            medicalUserInfoData.setContent(getContentByPosition(i, entity));
            MedicalUserInfoBean medicalUserInfoBean = new MedicalUserInfoBean(medicalUserInfoData);
            displayBeen.add(medicalUserInfoBean);
            if (i == 5) {
                displayBeen.add(propertyPlacardDividerLongBean);
            } else {
                displayBeen.add(propertyPlacardDividerBean);
            }
        }

        //空白布局
        displayBeen.add(medicalUserInfoBlackBean);

        //细长分割线
        displayBeen.add(propertyPlacardDividerLongBean);

        //中间的三个布局
        for (int i = 6; i <= 8; i++) {
            MedicalUserInfoData medicalUserInfoData = new MedicalUserInfoData();
            medicalUserInfoData.setLabel(getLabelByPosition(i));
            medicalUserInfoData.setContent(getContentByPosition(i, entity));
            MedicalUserInfoBean medicalUserInfoBean = new MedicalUserInfoBean(medicalUserInfoData);
            displayBeen.add(medicalUserInfoBean);
            if (i == 8) {
                displayBeen.add(propertyPlacardDividerLongBean);
            } else {
                displayBeen.add(propertyPlacardDividerBean);
            }
        }

        //空白布局
        displayBeen.add(medicalUserInfoBlackBean);

        //细长分割线
        displayBeen.add(propertyPlacardDividerLongBean);

        //中间的三个布局
        for (int i = 9; i <= 10; i++) {
            MedicalUserInfoData medicalUserInfoData = new MedicalUserInfoData();
            medicalUserInfoData.setLabel(getLabelByPosition(i));
            medicalUserInfoData.setContent(getContentByPosition(i, entity));
            MedicalUserInfoBean medicalUserInfoBean = new MedicalUserInfoBean(medicalUserInfoData);
            displayBeen.add(medicalUserInfoBean);
            if (i == 10) {
                displayBeen.add(propertyPlacardDividerLongBean);
            } else {
                displayBeen.add(propertyPlacardDividerBean);
            }
        }

        //空白布局
        displayBeen.add(medicalUserInfoBlackBean);

        return displayBeen;
    }

    private String getContentByPosition(int position, MedicalUserInfoEntity entity) {
        if (entity == null
                || entity.getData() == null) {
            return "";
        } else {
            switch (position) {
                case 1:
                    return TextUtils.isEmpty(entity.getData().getNickName()) ? "" : entity.getData().getNickName();
                case 2:
                    return TextUtils.isEmpty(entity.getData().getName()) ? "" : entity.getData().getName();
                case 3:
                    return TextUtils.isEmpty(entity.getData().getIdcard()) ? "" : entity.getData().getIdcard();
                case 4:
                    return TextUtils.isEmpty(entity.getData().getBirthday()) ? "" : entity.getData().getBirthday();
                case 5:
                    return TextUtils.isEmpty(entity.getData().getSex() + "") ? "" : entity.getData().getSex() + "";
                case 6:
                    return TextUtils.isEmpty(entity.getData().getHomenumber()) ? "" : entity.getData().getHomenumber();
                case 7:
                    return TextUtils.isEmpty(entity.getData().getPhone()) ? "" : entity.getData().getPhone();
                case 8:
                    return TextUtils.isEmpty(entity.getData().getEmail()) ? "" : entity.getData().getEmail();
                case 9:
                    return TextUtils.isEmpty(entity.getData().getHeight()) ? "" : entity.getData().getHeight();
                case 10:
                    return TextUtils.isEmpty(entity.getData().getWeight()) ? "" : entity.getData().getWeight();
                default:
                    return "";
            }
        }
    }

    private String getLabelByPosition(int position) {

        switch (position) {
            case 1:
                return getPresenter().getView().getString(R.string.medical_nickname);
            case 2:
                return getPresenter().getView().getString(R.string.medical_name);
            case 3:
                return getPresenter().getView().getString(R.string.medical_id_card);
            case 4:
                return getPresenter().getView().getString(R.string.medical_birthday);
            case 5:
                return getPresenter().getView().getString(R.string.medical_sex);
            case 6:
                return getPresenter().getView().getString(R.string.medical_home_phone_number);
            case 7:
                return getPresenter().getView().getString(R.string.medical_phone_number);
            case 8:
                return getPresenter().getView().getString(R.string.medical_email);
            case 9:
                return getPresenter().getView().getString(R.string.medical_height);
            case 10:
                return getPresenter().getView().getString(R.string.medical_weight);
            default:
                return "";
        }
    }

    public Observable<Result<MedicalStatusEntity>> changeUserInfo(Map<String, String> map) {
        return RetrofitHelper.<ServiceAPI>createMedicalConnection()
                .changeUserInfo(UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_TOKEN)
                        , map)
                .compose(CommonWrap.wrap());
    }

}
