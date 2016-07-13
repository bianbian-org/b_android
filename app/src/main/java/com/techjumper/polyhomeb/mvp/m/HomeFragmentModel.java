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
        list.add(R.mipmap.ic_launcher);
        list.add(R.mipmap.icon_laundry);
        list.add(R.mipmap.icon_complaint);
        list.add(R.mipmap.icon_friend);
        list.add(R.mipmap.icon_title_menu);
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
        juJiaData.setNotice("您有一万个快递正在派送中");
        JuJiaDataBean juJiaDataBean = new JuJiaDataBean(juJiaData);
        displayBeans.add(juJiaDataBean);

        //增加  智能家居  部分的数据  item = 3
        PolyHomeData polyHomeData = new PolyHomeData();
        polyHomeData.setSceneName1("哇咔咔");
        polyHomeData.setSceneName2("我嘞个擦");
        polyHomeData.setSceneName3("这 这就尴尬了");
        polyHomeData.setSceneName4("呵呵");
        PolyHomeDataBean polyHomeDataBean = new PolyHomeDataBean(polyHomeData);
        displayBeans.add(polyHomeDataBean);

        return displayBeans;

    }

}
