package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.JuJiaData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PolyHomeData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyData;
import com.techjumper.polyhomeb.adapter.recycler_Data.ViewPagerData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.JuJiaDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PolyHomeDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.ViewPagerDataBean;
import com.techjumper.polyhomeb.mvp.p.fragment.HomeFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

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

        //增加 物业 部分的数据    item = 1
        PropertyData propertyData = new PropertyData();
        propertyData.setNotice("通知:小区停水的重要的很的公告");
        PropertyDataBean propertyDataBean = new PropertyDataBean(propertyData);
        displayBeans.add(propertyDataBean);

        //增加 聚家 部分的数据    item = 2
        JuJiaData juJiaData = new JuJiaData();
        juJiaData.setNotice("您有2个快递正在派送中");
        JuJiaDataBean juJiaDataBean = new JuJiaDataBean(juJiaData);
        displayBeans.add(juJiaDataBean);

        //增加  智能家居  部分的数据  item = 3
        PolyHomeData polyHomeData = new PolyHomeData();
//        polyHomeData.setSceneName1("卧室照明");
//        polyHomeData.setSceneName2("二楼灯光");
//        polyHomeData.setSceneName3("客厅安防");
//        polyHomeData.setSceneName4("起夜模式");
        PolyHomeDataBean polyHomeDataBean = new PolyHomeDataBean(polyHomeData);
        displayBeans.add(polyHomeDataBean);

        return displayBeans;

    }

}
