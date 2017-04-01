package com.techjumper.polyhomeb.mvp.m;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.QueryFamilyEntity;
import com.techjumper.polyhomeb.entity.UserFamiliesAndVillagesEntity;
import com.techjumper.polyhomeb.entity.VillageLockEntity;
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

    public Observable<BluetoothLockDoorInfoEntity> getBLEDoorInfo() {
        KeyValuePair keyValuePair = KeyValueCreator.getBLEDoorInfo(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID));
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getBLEDoorInfo(map)
                .compose(CommonWrap.wrap());
    }

    public Observable<VillageLockEntity> getVillageLocks() {
        KeyValuePair keyValuePair = KeyValueCreator.getVillageLocks(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID));
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getVillageLocks(map)
                .compose(CommonWrap.wrap());
    }

    public Observable<QueryFamilyEntity> getCurrentFamilyAdminUserId() {
        KeyValuePair keyValuePair = KeyValueCreator.queryFamily(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID));
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .queryFamilyInfo(entity)
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
                UserManager.INSTANCE.updateFamilyOrVillageInfo(true, id + "", family_name, village_id + "");
                break;
            }
            return;
        }
        if (village_infos != null && village_infos.size() != 0) {
            for (UserFamiliesAndVillagesEntity.DataBean.VillageInfosBean bean : village_infos) {
                int village_id = bean.getVillage_id();
                String village_name = bean.getVillage_name();
                UserManager.INSTANCE.updateFamilyOrVillageInfo(false, village_id + "", village_name, village_id + "");
                break;
            }
        }
    }

    /**
     * 判断当前家庭或小区的信息在后台是否被删除（小艾）
     *
     * @param userFamiliesAndVillagesEntity
     */
    public void currentFamilyOrVillageIsDelete(UserFamiliesAndVillagesEntity userFamiliesAndVillagesEntity) {
        UserFamiliesAndVillagesEntity.DataBean data = userFamiliesAndVillagesEntity.getData();
        List<UserFamiliesAndVillagesEntity.DataBean.FamilyInfosBean> family_infos = data.getFamily_infos();
        List<UserFamiliesAndVillagesEntity.DataBean.VillageInfosBean> village_infos = data.getVillage_infos();
        /**
         * 存储的默认数据提取
         */
        String currentFamilyId = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID);
        String currentFamilyName = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_SHOW_TITLE_NAME);
        String currentVillageId = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID);

        /**
         * 获取家庭或小区中的第一条数据，以便默认家庭或小区信息确实被删除时，重新保存
         */
        int first_family_id = 0;
        String first_family_name = null;
        int first_village_id = 0;

        if (UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_SHOW_IS_FAMILY_OR_VILLAGE).equals(UserManager.VALUE_IS_FAMILY)) {
            /**
             * 1.代表当前默认为家庭
             * 2.判断请求返回的数据中，是否包含默认其数据，包含则代表后台没有删除其数据;不包含代表后台把其数据删除啦，需要重新定义默认的数据
             */
            if (family_infos != null && family_infos.size() != 0) {

                for (int i = 0; i < family_infos.size(); i++) {
                    UserFamiliesAndVillagesEntity.DataBean.FamilyInfosBean bean = family_infos.get(i);
                    int id = bean.getId();
                    String family_name = bean.getFamily_name();
                    int village_id = bean.getVillage_id();

                    if (currentFamilyId.equals(id + "") &&
                            currentFamilyName.equals(family_name) &&
                            currentVillageId.equals(village_id + "")) {
                        //当前默认家庭并没有被删除，不用理会,直接退出该方法
                        JLog.d("默认的家庭信息【没有】被删除");
                        return;
                    }
                    if (i == 0) {
                        first_family_id = id;
                        first_family_name = family_name;
                        first_village_id = village_id;
                    }
                }
                //如果能执行到此步骤，代表请求的数据中不包括默认的家庭数据需要重新定义
                UserManager.INSTANCE.updateFamilyOrVillageInfo(true, first_family_id + "", first_family_name, first_village_id + "");
                JLog.d("默认的家庭信息【已经】被删除，设置第一项家庭数据为默认家庭数据");
                return;
            } else {
                /**
                 * 1.代表后台把默认家庭的数据删除啦
                 * 2.因为请求的数据不为空，所以逻辑到达此处时，代表请求的数据中包含小区信息
                 * 3.取出第一项作为默认数据
                 */
                for (UserFamiliesAndVillagesEntity.DataBean.VillageInfosBean bean : village_infos) {
                    int village_id = bean.getVillage_id();
                    String village_name = bean.getVillage_name();
                    UserManager.INSTANCE.updateFamilyOrVillageInfo(false, village_id + "", village_name, village_id + "");
                    JLog.d("默认的家庭信息【已经】被删除且没有家庭数据啦，设置第一项小区数据为默认小区数据");
                    return;
                }
            }

        } else {
            /**
             * 1.代表当前默认为小区
             * 2.判断请求返回的数据中，是否包含默认其数据，包含则代表后台没有删除其数据;不包含代表后台把其数据删除啦，需要重新定义默认的数据
             * 3.重新设定默认数据时，有两种情况
             * <p>
             * 3.1先判定是否存在家庭数据，存在取第一项为默认数据
             * 3.2如果家庭数据不存在，则取小区的第一项作为默认数据
             * <p>
             * 4.没有小区信息则代表已经删除默认信息，取家庭第一项数据作为默认数据
             */
            if (village_infos != null && village_infos.size() != 0) {

                for (int i = 0; i < village_infos.size(); i++) {
                    UserFamiliesAndVillagesEntity.DataBean.VillageInfosBean bean = village_infos.get(i);
                    int village_id = bean.getVillage_id();
                    String village_name = bean.getVillage_name();

                    if (currentFamilyId.equals(village_id + "") &&
                            currentFamilyName.equals(village_name) &&
                            currentVillageId.equals(village_id + "")) {
                        //当前默认小区并没有被删除，不用理会,直接退出该方法
                        JLog.d("默认的小区信息【没有】被删除");
                        return;
                    }
                    if (i == 0) {
                        first_family_id = village_id;
                        first_family_name = village_name;
                        first_village_id = village_id;
                    }
                }

                //如果能执行到此步骤，代表请求的数据中不包括默认的家庭数据需要重新定义
                if (family_infos != null && family_infos.size() != 0) {
                    for (int i = 0; i < family_infos.size(); i++) {
                        UserFamiliesAndVillagesEntity.DataBean.FamilyInfosBean bean = family_infos.get(i);
                        int id = bean.getId();
                        String family_name = bean.getFamily_name();
                        int village_id = bean.getVillage_id();
                        UserManager.INSTANCE.updateFamilyOrVillageInfo(true, id + "", family_name, village_id + "");
                        JLog.d("默认的小区信息【已经】被删除,且有家庭信息，设置第一项家庭数据为默认家庭数据");
                        return;
                    }

                } else {
                    UserManager.INSTANCE.updateFamilyOrVillageInfo(true, first_family_id + "", first_family_name, first_village_id + "");
                    JLog.d("默认的小区信息【已经】被删除,且没有家庭信息，设置第一项小区数据为默认小区数据");
                    return;
                }
            } else {

                /**
                 * 1.代表后台把默认小区的数据删除啦
                 * 2.因为请求的数据不为空，所以逻辑到达此处时，代表请求的数据中包含家庭信息
                 * 3.取出第一项作为默认数据
                 */
                for (int i = 0; i < family_infos.size(); i++) {
                    UserFamiliesAndVillagesEntity.DataBean.FamilyInfosBean bean = family_infos.get(i);
                    int id = bean.getId();
                    String family_name = bean.getFamily_name();
                    int village_id = bean.getVillage_id();
                    UserManager.INSTANCE.updateFamilyOrVillageInfo(true, id + "", family_name, village_id + "");
                    JLog.d("默认的小区信息【已经】被删除,且没有小区信息，设置第一项家庭数据为默认家庭数据");
                    return;
                }

            }
        }
    }
}
