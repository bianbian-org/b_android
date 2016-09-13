package com.techjumper.polyhomeb.mvp.m;

import android.text.TextUtils;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.others.KeyValuePair;
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
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.MedicalUserInfoEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalUserInfoActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalUserInfoActivityModel extends BaseModel<MedicalUserInfoActivityPresenter> {

    public static final int CANCLICK = 1;
    public static final int CANNOTCLICK = 2;

    public MedicalUserInfoActivityModel(MedicalUserInfoActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<MedicalUserInfoEntity> getInfo() {
        KeyValuePair keyValuePair = KeyValueCreator.getMedicalUserInfo(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket());

        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getMedicalUserInfo(entity)
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

        //短分割线
        PropertyPlacardDividerData propertyPlacardDividerData = new PropertyPlacardDividerData();
        propertyPlacardDividerData.setMarginLeft(RuleUtils.dp2Px(30));//和布局中文字的marginLeft相同
        PropertyPlacardDividerBean propertyPlacardDividerBean = new PropertyPlacardDividerBean(propertyPlacardDividerData);
        displayBeen.add(propertyPlacardDividerBean);

        //前面的五个布局
        for (int i = 1; i <= 5; i++) {
            MedicalUserInfoData medicalUserInfoData = new MedicalUserInfoData();
            medicalUserInfoData.setLabel(getLabelByPosition(i));
            medicalUserInfoData.setContent(getContentByPosition(i, entity));
            medicalUserInfoData.setType(CANCLICK);
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
            medicalUserInfoData.setType(CANCLICK);
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
            medicalUserInfoData.setType(CANCLICK);
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
                    return TextUtils.isEmpty(entity.getData().getSex()) ? "" : entity.getData().getSex();
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

}
