package com.techjumper.polyhomeb.mvp.m;

import android.text.TextUtils;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyComplainTitleData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairPropertyResponseData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyComplainTitleBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairPropertyResponseBean;
import com.techjumper.polyhomeb.entity.PropertyRepairEntity;
import com.techjumper.polyhomeb.mvp.p.fragment.ComplainFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ComplainFragmentModel extends BaseModel<ComplainFragmentPresenter> {

    public ComplainFragmentModel(ComplainFragmentPresenter presenter) {
        super(presenter);
    }

    private PropertyRepairEntity fake() {
        PropertyRepairEntity propertyRepairEntity = new PropertyRepairEntity();
        PropertyRepairEntity.DataBean dataBean = new PropertyRepairEntity.DataBean();

        List<PropertyRepairEntity.DataBean.ListBean> listBeanList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            PropertyRepairEntity.DataBean.ListBean listBean = new PropertyRepairEntity.DataBean.ListBean();
            listBean.setContent("这是内容" + i + "....");
            listBean.setTime("12月" + i + "日");
            listBean.setRead(i == 2 ? true : false);
            listBean.setPropertyResponse(i != 2 ? null : "物业回复:南海是中国的");
            listBean.setTitle("标题" + i);
            listBean.setBtnName(i == 2 ? "已回复" : i == 3 ? "已提交" : "已完成");
            listBeanList.add(listBean);
        }

        dataBean.setList(listBeanList);
        propertyRepairEntity.setData(dataBean);

        return propertyRepairEntity;
    }

    public List<DisplayBean> initPlacardData() {

        PropertyRepairEntity fake = fake();
        PropertyRepairEntity.DataBean data = fake.getData();
        List<PropertyRepairEntity.DataBean.ListBean> list = data.getList();

        if (list == null || list.size() == 0) return null;

        List<DisplayBean> displayBeen = new ArrayList<>();

        //第0个item,大分割线
        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        displayBeen.add(propertyRepairBigDividerBean);

        //第一个item,带总数量什么的
        PropertyComplainTitleData propertyComplainTitleData = new PropertyComplainTitleData();
        propertyComplainTitleData.setTitle("反馈记录");
        propertyComplainTitleData.setCount(list.size());
        PropertyComplainTitleBean propertyComplainTitleBean = new PropertyComplainTitleBean(propertyComplainTitleData);
        displayBeen.add(propertyComplainTitleBean);

        //第二个item,长的分割线
        PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
        PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
        displayBeen.add(propertyPlacardDividerLongBean);

        for (int i = 0; i < list.size(); i++) {

            PropertyRepairEntity.DataBean.ListBean listBean = list.get(i);

            //第三个item,内容
            PropertyRepairContentData propertyRepairContentData = new PropertyRepairContentData();
            propertyRepairContentData.setTitle(listBean.getTitle());
            propertyRepairContentData.setRead(listBean.isRead());
            propertyRepairContentData.setBtnName(listBean.getBtnName());
            propertyRepairContentData.setTime(listBean.getTime());
            propertyRepairContentData.setContent(listBean.getContent());
            PropertyRepairContentBean propertyRepairContentBean = new PropertyRepairContentBean(propertyRepairContentData);
            displayBeen.add(propertyRepairContentBean);

            if (TextUtils.isEmpty(listBean.getPropertyResponse())) {
                //如果物业回复是空的,那么直接加一个大的分割线
                //如果当前的listBean是总数据的最后一个,那么就不能加大的分割线了
                if (i != list.size() - 1) {
                    displayBeen.add(propertyRepairBigDividerBean);
                }

            } else {
                //第四个item,短点的分割线
                PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                displayBeen.add(dividerBean);

                //第五个item,物业回复
                PropertyRepairPropertyResponseData propertyRepairPropertyResponseData = new PropertyRepairPropertyResponseData();
                propertyRepairPropertyResponseData.setResponse(listBean.getPropertyResponse());
                PropertyRepairPropertyResponseBean propertyRepairPropertyResponseBean = new PropertyRepairPropertyResponseBean(propertyRepairPropertyResponseData);
                displayBeen.add(propertyRepairPropertyResponseBean);

                //如果物业回复不是空的话,那么在短点的分割线以及物业回复之后再加大的分割线
                //如果当前的listBean是总数据的最后一个,那么就不能加大的分割线了
                if (i != list.size() - 1) {
                    displayBeen.add(propertyRepairBigDividerBean);
                }
            }
        }

        return displayBeen;
    }
}
