package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.adapter.recycler_Data.ChooseVillageData;
import com.techjumper.polyhomeb.adapter.recycler_Data.ChooseVillageFamilyData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.ChooseVillageBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.ChooseVillageFamilyBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.entity.event.VillageEntity;
import com.techjumper.polyhomeb.mvp.p.activity.ChooseVillageFamilyActivityPresenter;
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
 * Date: 16/8/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChooseVillageFamilyActivityModel extends BaseModel<ChooseVillageFamilyActivityPresenter> {

    private List<String> mProvinces = new ArrayList<>();
    private List<VillageEntity.DataBean.InfosBean.VillagesBean> mNamesAndIds = new ArrayList<>();
    private VillageEntity mVillageEntity;

    public ChooseVillageFamilyActivityModel(ChooseVillageFamilyActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public int getComeFrom() {
        if (getExtra() != null) {
            return getExtra().getInt(Constant.KEY_COME_FROM, -1);
        } else {
            return -1;
        }
    }

    public Observable<VillageEntity> getVillages() {
        KeyValuePair keyValuePair = KeyValueCreator.getVillages(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket());
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getVillages(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }

    public void processData(VillageEntity villageEntity) {
        mProvinces.clear();
        mNamesAndIds.clear();
        this.mVillageEntity = villageEntity;
        List<VillageEntity.DataBean.InfosBean> infos = villageEntity.getData().getInfos();
        for (VillageEntity.DataBean.InfosBean bean : infos) {
            String province = bean.getProvince();
            mProvinces.add(province);
            mNamesAndIds.addAll(bean.getVillages());
        }
    }

    public List<String> getProvinces() {
        return mProvinces;
    }

    public List<VillageEntity.DataBean.InfosBean.VillagesBean> getNamesAndIds() {
        return mNamesAndIds;
    }

    public List<DisplayBean> getRvProvinceDatas() {
        if (mProvinces == null || mProvinces.size() == 0) return null;
        List<DisplayBean> displayBean = new ArrayList<>();

        for (int i = 0; i < mProvinces.size(); i++) {
            ChooseVillageFamilyData chooseVillageFamilyData = new ChooseVillageFamilyData();
            chooseVillageFamilyData.setName(mProvinces.get(i));
            if (i == getPresenter().getView().sCurrentIndex) {
                chooseVillageFamilyData.setChoosed(true);
            }
            ChooseVillageFamilyBean chooseVillageFamilyBean = new ChooseVillageFamilyBean(chooseVillageFamilyData);
            displayBean.add(chooseVillageFamilyBean);
        }
        return displayBean;
    }

    public List<DisplayBean> getRvVillageDatas(int currentIndex) {
        if (mNamesAndIds == null || mNamesAndIds.size() == 0) return null;
        List<DisplayBean> displayBean = new ArrayList<>();

        //取出当前省份下的小区
        List<VillageEntity.DataBean.InfosBean> infos = mVillageEntity.getData().getInfos();
        VillageEntity.DataBean.InfosBean infosBean = infos.get(currentIndex);
        List<VillageEntity.DataBean.InfosBean.VillagesBean> villages = infosBean.getVillages();

        for (int i = 0; i < villages.size(); i++) {
            if (i == 0) {
                //长分割线
                PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
                PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
                displayBean.add(propertyPlacardDividerLongBean);
            } else {
                //短分割线
                PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                dividerData.setMarginLeft(RuleUtils.dp2Px(14));
                PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                displayBean.add(dividerBean);
            }
            ChooseVillageData chooseVillageData = new ChooseVillageData();
            chooseVillageData.setName(villages.get(i).getName());
            chooseVillageData.setId(villages.get(i).getId());
            ChooseVillageBean chooseVillageBean = new ChooseVillageBean(chooseVillageData);
            displayBean.add(chooseVillageBean);

            if (i == villages.size() - 1) {
                //长分割线
                PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
                PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
                displayBean.add(propertyPlacardDividerLongBean);
            } else {
                //短分割线
                PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                dividerData.setMarginLeft(RuleUtils.dp2Px(14));
                PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                displayBean.add(dividerBean);
            }
        }

        return displayBean;

    }
}
