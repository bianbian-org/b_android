package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.BluetoothData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PolyHomeData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.ViewPagerData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.BluetoothBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PolyHomeDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.ViewPagerDataBean;
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
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.ad1);
        list.add(R.mipmap.ad2);
        list.add(R.mipmap.ad3);
        data.setDrawables(list);
        ViewPagerDataBean viewPagerDataBean = new ViewPagerDataBean(data);
        displayBeans.add(viewPagerDataBean);

        //蓝牙开锁部分的item    item = 1
        BluetoothData bluetoothData = new BluetoothData();
        bluetoothData.setInfosBeen(UserManager.INSTANCE.getBLEInfo());
        bluetoothData.setCommunitySupportBleDoor(UserManager.INSTANCE.isCurrentCommunitySupportBLEDoor());
        BluetoothBean bluetoothBean = new BluetoothBean(bluetoothData);
        displayBeans.add(bluetoothBean);

        //增加 物业 部分的数据    item = 2
        PropertyData propertyData = new PropertyData();
        propertyData.setNotice(null);
        PropertyDataBean propertyDataBean = new PropertyDataBean(propertyData);
        displayBeans.add(propertyDataBean);

        //分割线
//        displayBeans.add(propertyRepairBigDividerBean);
//
//        //增加 聚家 部分的数据    item = 3
//        JuJiaData juJiaData = new JuJiaData();
//        juJiaData.setNotice("您有2个快递正在派送中");
//        JuJiaDataBean juJiaDataBean = new JuJiaDataBean(juJiaData);
//        displayBeans.add(juJiaDataBean);

        //分割线
        displayBeans.add(propertyRepairBigDividerBean);

        //增加  智能家居  部分的数据  item = 4
        PolyHomeData polyHomeData = new PolyHomeData();
        PolyHomeDataBean polyHomeDataBean = new PolyHomeDataBean(polyHomeData);
        displayBeans.add(polyHomeDataBean);

        return displayBeans;

    }

    public Observable<MarqueeTextInfoEntity> getMarqueeText() {
        KeyValuePair keyValuePair = KeyValueCreator.getMarqueeText(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID));
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getMarqueeText(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }
}
