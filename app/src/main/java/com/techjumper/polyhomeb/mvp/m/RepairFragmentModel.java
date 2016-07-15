package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairPropertyResponseData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairTitleData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairPropertyResponseBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairTitleBean;
import com.techjumper.polyhomeb.mvp.p.fragment.RepairFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RepairFragmentModel extends BaseModel<RepairFragmentPresenter> {

    public RepairFragmentModel(RepairFragmentPresenter presenter) {
        super(presenter);
    }

    public List<DisplayBean> initPlacardData() {

        List<DisplayBean> displayBeen = new ArrayList<>();

        //第一个item,带总数量什么的
        PropertyRepairTitleData propertyRepairTitleData = new PropertyRepairTitleData();
        propertyRepairTitleData.setTitle("报修记录");
        PropertyRepairTitleBean propertyRepairTitleBean = new PropertyRepairTitleBean(propertyRepairTitleData);
        displayBeen.add(propertyRepairTitleBean);

        //第二个item,长的分割线
        PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
        PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
        displayBeen.add(propertyPlacardDividerLongBean);

        //第三个item,内容
        PropertyRepairContentData propertyRepairContentData = new PropertyRepairContentData();
        propertyRepairContentData.setTitle("这是标题");
        propertyRepairContentData.setRead(false);
        propertyRepairContentData.setBtnName("已回复");
        propertyRepairContentData.setTime("这是时间");
        propertyRepairContentData.setContent("这是内容啊哈哈哈哈哈哈哈这就尴尬了rgrrgdewfwe和哈哈");
        PropertyRepairContentBean propertyRepairContentBean = new PropertyRepairContentBean(propertyRepairContentData);
        displayBeen.add(propertyRepairContentBean);

        //第四个item,短点的分割线
        PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
        PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
        displayBeen.add(dividerBean);

        //第五个item,物业回复
        PropertyRepairPropertyResponseData propertyRepairPropertyResponseData = new PropertyRepairPropertyResponseData();
        propertyRepairPropertyResponseData.setResponse("物业回复:南海是中国的");
        PropertyRepairPropertyResponseBean propertyRepairPropertyResponseBean = new PropertyRepairPropertyResponseBean(propertyRepairPropertyResponseData);
        displayBeen.add(propertyRepairPropertyResponseBean);

        return displayBeen;
    }
}
