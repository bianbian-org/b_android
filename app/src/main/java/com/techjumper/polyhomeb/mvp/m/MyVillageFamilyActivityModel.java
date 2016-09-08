package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.databean.MyVillageFamilyBean;
import com.techjumper.polyhomeb.adapter.recycler_Data.MyVillageFamilyData;
import com.techjumper.polyhomeb.adapter.recycler_Data.MyVillageFamilyTextData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MyVillageFamilyTextBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MyVillageFamilyActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MyVillageFamilyActivityModel extends BaseModel<MyVillageFamilyActivityPresenter> {

    public MyVillageFamilyActivityModel(MyVillageFamilyActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<UserFamiliesAndVillagesEntity> getFamilyAndVillage() {
        KeyValuePair keyValuePair = KeyValueCreator.getFamilyAndVillage(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket());
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getFamilyAndVillage(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> processData(UserFamiliesAndVillagesEntity userFamiliesAndVillagesEntity) {

        List<DisplayBean> displayBeen = new ArrayList<>();

        List<UserFamiliesAndVillagesEntity.DataBean.FamilyInfosBean> family_infos = userFamiliesAndVillagesEntity.getData().getFamily_infos();
        List<UserFamiliesAndVillagesEntity.DataBean.VillageInfosBean> village_infos = userFamiliesAndVillagesEntity.getData().getVillage_infos();

        if (family_infos.size() != 0) {
            //文字
            MyVillageFamilyTextData myVillageFamilyTextData = new MyVillageFamilyTextData();
            myVillageFamilyTextData.setText(getPresenter().getView().getString(R.string.family));
            MyVillageFamilyTextBean myVillageFamilyTextBean = new MyVillageFamilyTextBean(myVillageFamilyTextData);
            displayBeen.add(myVillageFamilyTextBean);

            for (int i = 0; i < family_infos.size(); i++) {
                if (i == 0) {
                    //长分割线
                    PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
                    PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    //短分割线
                    PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                    dividerData.setMarginLeft(RuleUtils.dp2Px(14));
                    PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                    displayBeen.add(dividerBean);
                }
                MyVillageFamilyData myVillageFamilyData = new MyVillageFamilyData();
                myVillageFamilyData.setName(family_infos.get(i).getFamily_name());
                myVillageFamilyData.setFamily_id(family_infos.get(i).getId());
                myVillageFamilyData.setVillageId(family_infos.get(i).getId());
                myVillageFamilyData.setFamilyData(0);
                String isFamilyOrVillage = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE);
                if (isFamilyOrVillage.equals(UserManager.VALUE_IS_FAMILY)) {
                    if (family_infos.get(i).getId() == Integer.parseInt(UserManager.INSTANCE.getCurrentId())) {
                        myVillageFamilyData.setChoosed(true);
                    }
                }
                MyVillageFamilyBean myVillageFamilyBean = new MyVillageFamilyBean(myVillageFamilyData);
                displayBeen.add(myVillageFamilyBean);
                if (i == family_infos.size() - 1) {
                    //长分割线
                    PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
                    PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    //短分割线
                    PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                    dividerData.setMarginLeft(RuleUtils.dp2Px(14));
                    PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                    displayBeen.add(dividerBean);
                }
            }
        }

        if (village_infos.size() != 0) {
            //文字
            MyVillageFamilyTextData myVillageFamilyTextData = new MyVillageFamilyTextData();
            myVillageFamilyTextData.setText(getPresenter().getView().getString(R.string.village));
            MyVillageFamilyTextBean myVillageFamilyTextBean = new MyVillageFamilyTextBean(myVillageFamilyTextData);
            displayBeen.add(myVillageFamilyTextBean);

            for (int i = 0; i < village_infos.size(); i++) {
                if (i == 0) {
                    //长分割线
                    PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
                    PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    //短分割线
                    PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                    dividerData.setMarginLeft(RuleUtils.dp2Px(14));
                    PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                    displayBeen.add(dividerBean);
                }
                MyVillageFamilyData myVillageFamilyData = new MyVillageFamilyData();
                myVillageFamilyData.setName(village_infos.get(i).getVillage_name());
                myVillageFamilyData.setFamily_id(village_infos.get(i).getVillage_id());
                myVillageFamilyData.setVillageId(village_infos.get(i).getVillage_id());
                myVillageFamilyData.setVerified(village_infos.get(i).getVerified());
                myVillageFamilyData.setFamilyData(1);
                String isFamilyOrVillage = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE);
                if (isFamilyOrVillage.equals(UserManager.VALUE_IS_VILLAGE)) {
                    if (village_infos.get(i).getVillage_id() == Integer.parseInt(UserManager.INSTANCE.getCurrentId())) {
                        myVillageFamilyData.setChoosed(true);
                    }
                }
                MyVillageFamilyBean myVillageFamilyBean = new MyVillageFamilyBean(myVillageFamilyData);
                displayBeen.add(myVillageFamilyBean);

                if (i == village_infos.size() - 1) {
                    //长分割线
                    PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
                    PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
                    displayBeen.add(propertyPlacardDividerLongBean);
                } else {
                    //短分割线
                    PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                    dividerData.setMarginLeft(RuleUtils.dp2Px(14));
                    PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                    displayBeen.add(dividerBean);
                }
            }
        }
        return displayBeen;
    }
}
