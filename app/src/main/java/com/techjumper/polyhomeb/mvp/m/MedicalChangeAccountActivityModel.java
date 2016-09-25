package com.techjumper.polyhomeb.mvp.m;

import android.text.TextUtils;
import android.util.Base64;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalChangeAccountData;
import com.techjumper.polyhomeb.adapter.recycler_Data.MedicalUserInfoBlackData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MedicalChangeAccountBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MedicalUserInfoBlackBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.entity.medicalEntity.BaseArgumentsMedicalEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalAllUserEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserLoginEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangeAccountActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Subscriber;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalChangeAccountActivityModel extends BaseModel<MedicalChangeAccountActivityPresenter> {

    public MedicalChangeAccountActivityModel(MedicalChangeAccountActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<List<DisplayBean>> getSpData() {
        return Observable
                .create(new Observable.OnSubscribe<List<DisplayBean>>() {
                    @Override
                    public void call(Subscriber<? super List<DisplayBean>> subscriber) {
                        subscriber.onNext(getData(UserManager.INSTANCE.getMedicalAllUserInfo()));
                        subscriber.onCompleted();
                    }
                })
                .compose(CommonWrap.wrap());
    }

    private List<DisplayBean> getData(List<MedicalAllUserEntity> list) {
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

        for (int i = 0; i < list.size(); i++) {
            MedicalAllUserEntity medicalAllUserEntity = list.get(i);
            String currentUserId = UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_ID);
            String userId = medicalAllUserEntity.getUsername();

            MedicalChangeAccountData data = new MedicalChangeAccountData();
            data.setName(TextUtils.isEmpty(medicalAllUserEntity.getpName())
                    ? medicalAllUserEntity.getNickName() : medicalAllUserEntity.getpName());
            data.setCurrentAccount((currentUserId.equals(userId)) ? true : false);
            data.setUserId(userId);
            MedicalChangeAccountBean bean = new MedicalChangeAccountBean(data);
            if (list.size() == 1) {
                displayBeen.add(bean);
            } else {
                displayBeen.add(bean);
                if (i != list.size() - 1) {
                    displayBeen.add(propertyPlacardDividerBean);
                }
            }
        }

        displayBeen.add(propertyPlacardDividerLongBean);

        return displayBeen;
    }

    public Observable<Result<MedicalUserLoginEntity>> login(String username, String password) {
        KeyValuePair keyValuePair = KeyValueCreator.medicalUserLogin(1, 1);
        BaseArgumentsMedicalEntity medicalUserLoginArguments
                = NetHelper.createMedicalUserLoginArguments(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createMedicalConnection()
                .medicalUserLogin(encode(username, password), medicalUserLoginArguments)
                .compose(CommonWrap.wrap());
    }

    private String encode(String username, String password) {
        String encoded = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
        String authorization = "Basic" + " " + encoded;
        return authorization;
    }

}
