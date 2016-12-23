package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.adapter.recycler_Data.BluetoothData;
import com.techjumper.polyhomeb.adapter.recycler_Data.DnakeData;
import com.techjumper.polyhomeb.adapter.recycler_Data.JuJiaData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PolyHomeData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.ViewPagerData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.BluetoothBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.DnakeBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.JuJiaDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PolyHomeDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.ViewPagerDataBean;
import com.techjumper.polyhomeb.entity.ADEntity;
import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;
import com.techjumper.polyhomeb.entity.JuJiaInfoEntity;
import com.techjumper.polyhomeb.entity.MarqueeTextInfoEntity;
import com.techjumper.polyhomeb.mvp.p.fragment.HomeFragmentPresenter;
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
 * Date: 16/7/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeFragmentModel extends BaseModel<HomeFragmentPresenter> {

    public HomeFragmentModel(HomeFragmentPresenter presenter) {
        super(presenter);
    }

    public List<DisplayBean> initPropertyData() {

        //大分割线
        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);

        //总的数据源
        List<DisplayBean> displayBeans = new ArrayList<>();

        //增加ViewPager轮播图数据   item = 0
        ViewPagerData data = new ViewPagerData();
        ADEntity adEntity1 = new ADEntity();
        ADEntity.DataBean bean = new ADEntity.DataBean();
        List<ADEntity.DataBean.AdInfosBean> list = new ArrayList<>();
        ADEntity.DataBean.AdInfosBean adInfosBean = new ADEntity.DataBean.AdInfosBean();
        adInfosBean.setUrl(null);
        adInfosBean.setMedia_type(1);
        adInfosBean.setMedia_url(null);
        list.add(adInfosBean);
        bean.setAd_infos(list);
        adEntity1.setData(bean);
        data.setAdInfos(adEntity1);

        ViewPagerDataBean viewPagerDataBean = new ViewPagerDataBean(data);
        displayBeans.add(viewPagerDataBean);

        //蓝牙开锁部分的item    item = 1
        BluetoothData bluetoothData = new BluetoothData();
        bluetoothData.setInfosBeen(UserManager.INSTANCE.getBLEInfo());
        bluetoothData.setCommunitySupportBleDoor(UserManager.INSTANCE.isCurrentCommunitySupportBLEDoor());
        BluetoothBean bluetoothBean = new BluetoothBean(bluetoothData);
        displayBeans.add(bluetoothBean);

        //Dnake开锁部分item
        DnakeData dnakeData = new DnakeData();
        dnakeData.setCommunitySupportDnakeDoor(UserManager.INSTANCE.isCurrentCommunitySupportDnakeDoor());
        DnakeBean dnakeBean = new DnakeBean(dnakeData);
        displayBeans.add(dnakeBean);

        //增加 物业 部分的数据    item = 2
        PropertyData propertyData = new PropertyData();
        propertyData.setNotice(null);
        PropertyDataBean propertyDataBean = new PropertyDataBean(propertyData);
        displayBeans.add(propertyDataBean);

        //分割线在聚家视图中已经处理了
        //增加 聚家 部分的数据    item = 3
        JuJiaData juJiaData = new JuJiaData();
        juJiaData.setEntity(null);
        JuJiaDataBean juJiaDataBean = new JuJiaDataBean(juJiaData);
        displayBeans.add(juJiaDataBean);

        //分割线
        displayBeans.add(propertyRepairBigDividerBean);

        //增加  智能家居  部分的数据  item = 4
        PolyHomeData polyHomeData = new PolyHomeData();
        PolyHomeDataBean polyHomeDataBean = new PolyHomeDataBean(polyHomeData);
        displayBeans.add(polyHomeDataBean);

        return displayBeans;

    }

    public Observable<MarqueeTextInfoEntity> getMarqueeText() {
        boolean family = UserManager.INSTANCE.isFamily();
        String family_id;
        String village_id;
        if (family) {
            family_id = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID);
            village_id = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID);
        } else {
            village_id = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID);
            family_id = "";
        }
        KeyValuePair keyValuePair = KeyValueCreator.getMarqueeText(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , village_id
                , family_id);
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getMarqueeText(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }

    public Observable<ADEntity> getADInfo() {
        KeyValuePair keyValuePair = KeyValueCreator.getADInfo(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID));
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getADInfo(map)
                .compose(CommonWrap.wrap());
    }

    public Observable<JuJiaInfoEntity> getJuJiaInfo() {
        KeyValuePair keyValuePair = KeyValueCreator.getJuJiaInfo(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID));
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getJuJiaInfo(map)
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
}
