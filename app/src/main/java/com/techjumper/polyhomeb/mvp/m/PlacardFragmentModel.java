package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardTimeLineData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardTimeLineBean;
import com.techjumper.polyhomeb.entity.PropertyPlacardEntity;
import com.techjumper.polyhomeb.mvp.p.fragment.PlacardFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PlacardFragmentModel extends BaseModel<PlacardFragmentPresenter> {

    public PlacardFragmentModel(PlacardFragmentPresenter presenter) {
        super(presenter);
    }

    private PropertyPlacardEntity fakeEntity() {
        PropertyPlacardEntity entity = new PropertyPlacardEntity();

        List<PropertyPlacardEntity.DataBean.ListBean.DatasBean.SencesBean> sencesBeanList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            PropertyPlacardEntity.DataBean.ListBean.DatasBean.SencesBean sencesBean = new PropertyPlacardEntity.DataBean.ListBean.DatasBean.SencesBean();
            sencesBean.setBtn_name(i == 2 ? "公告" : "资讯");
            sencesBean.setContent("这些是内容" + i + "为丰富和企鹅和放弃而话费卡多出去额空间发过去诶哦而且就放弃而返回去金额防火墙而房价和企鹅哦");
            sencesBean.setTime("12月" + i + "日");
            sencesBean.setTitle("这是标题" + i + "而非和勤奋和气");
            sencesBeanList.add(sencesBean);
        }

        List<PropertyPlacardEntity.DataBean.ListBean.DatasBean> datasBeanList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            PropertyPlacardEntity.DataBean.ListBean.DatasBean datasBean = new PropertyPlacardEntity.DataBean.ListBean.DatasBean();
            datasBean.setSences(sencesBeanList);
            datasBeanList.add(datasBean);
        }

        List<PropertyPlacardEntity.DataBean.ListBean> listBeanList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            PropertyPlacardEntity.DataBean.ListBean listBean = new PropertyPlacardEntity.DataBean.ListBean();
            listBean.setTime(12 + "月");
            listBean.setDatas(datasBeanList);
            listBeanList.add(listBean);
        }

        PropertyPlacardEntity.DataBean dataBean = new PropertyPlacardEntity.DataBean();
        dataBean.setList(listBeanList);

        entity.setData(dataBean);

        return entity;
    }


    public List<DisplayBean> initPlacardData() {

        PropertyPlacardEntity propertyPlacardEntity = fakeEntity();
        PropertyPlacardEntity.DataBean data1 = propertyPlacardEntity.getData();
        List<PropertyPlacardEntity.DataBean.ListBean> list1 = data1.getList();


        //总的数据源
        List<DisplayBean> displayBeans = new ArrayList<>();

        for (int i = 0; i < list1.size(); i++) {
            PropertyPlacardEntity.DataBean.ListBean listBean = list1.get(i);
            String time = listBean.getTime();
            List<PropertyPlacardEntity.DataBean.ListBean.DatasBean> datas = listBean.getDatas();

            PropertyPlacardTimeLineData timeLineData = new PropertyPlacardTimeLineData();

            timeLineData.setTime(time);

            PropertyPlacardTimeLineBean timeLineBean = new PropertyPlacardTimeLineBean(timeLineData);
            displayBeans.add(timeLineBean);

            for (int j = 0; j < datas.size(); j++) {
                List<PropertyPlacardEntity.DataBean.ListBean.DatasBean.SencesBean> sences = datas.get(j).getSences();

                for (int k = 0; k < sences.size(); k++) {
                    PropertyPlacardContentData contentData = new PropertyPlacardContentData();
                    contentData.setTime(sences.get(k).getTime());
                    contentData.setBtnName(sences.get(k).getBtn_name());
                    contentData.setContent(sences.get(k).getContent());
                    contentData.setRead(false);
                    contentData.setTitle(sences.get(k).getTitle());

                    PropertyPlacardContentBean contentBean = new PropertyPlacardContentBean(contentData);

                    displayBeans.add(contentBean);

                    if (sences.size() != 1 & k != sences.size() - 1) { //只有一条数据的时候没有分割线,最后一条数据下面也没有分割线
                        PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                        PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                        displayBeans.add(dividerBean);
                    }
                }
            }
        }
        return displayBeans;
    }
}
