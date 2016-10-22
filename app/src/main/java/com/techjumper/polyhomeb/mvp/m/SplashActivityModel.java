package com.techjumper.polyhomeb.mvp.m;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.mvp.p.activity.SplashActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/28
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SplashActivityModel extends BaseModel<SplashActivityPresenter> {

    public SplashActivityModel(SplashActivityPresenter presenter) {
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

    public Observable<BluetoothLockDoorInfoEntity> getBLEDoorInfo(String village_id, String family_id) {
        KeyValuePair keyValuePair = KeyValueCreator.getBLEDoorInfo(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , village_id
                , family_id);
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getBLEDoorInfo(map)
                .compose(CommonWrap.wrap());
    }

    public void saveDataToSp(UserFamiliesAndVillagesEntity userFamiliesAndVillagesEntity) {
        UserFamiliesAndVillagesEntity.DataBean data = userFamiliesAndVillagesEntity.getData();
        List<UserFamiliesAndVillagesEntity.DataBean.VillageInfosBean> village_infos = data.getVillage_infos();
        List<UserFamiliesAndVillagesEntity.DataBean.FamilyInfosBean> family_infos = data.getFamily_infos();
        //优先取出家庭的存起来,如果家庭的是空,则取小区的数据存起来
        //默认都取出第一项
        if (family_infos != null && family_infos.size() != 0) {
            for (UserFamiliesAndVillagesEntity.DataBean.FamilyInfosBean bean : family_infos) {
                int id = bean.getId();
                String family_name = bean.getFamily_name();
                int village_id = bean.getVillage_id();
                UserManager.INSTANCE.updateFamilyOrVillageInfo(true, id + "", family_name, village_id+"");
                break;
            }
            return;
        }
        if (village_infos != null && village_infos.size() != 0) {
            for (UserFamiliesAndVillagesEntity.DataBean.VillageInfosBean bean : village_infos) {
                int village_id = bean.getVillage_id();
                String village_name = bean.getVillage_name();
                UserManager.INSTANCE.updateFamilyOrVillageInfo(false, village_id + "", village_name, village_id+"");
                break;
            }
        }
    }
}
