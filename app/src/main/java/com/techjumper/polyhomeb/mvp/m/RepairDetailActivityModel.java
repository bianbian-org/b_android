package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.IMEmptyViewData;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewRepairDetailChoosedPicData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailPropertyContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailProprietorContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.RepairDetailTimeData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.IMEmptyViewBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewRepairDetailChoosedPicBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairDetailPropertyBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairDetailProprietorContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.RepairDetailTimeBean;
import com.techjumper.polyhomeb.mvp.p.activity.RepairDetailActivityPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RepairDetailActivityModel extends BaseModel<RepairDetailActivityPresenter> {

    public RepairDetailActivityModel(RepairDetailActivityPresenter presenter) {
        super(presenter);
    }

    public List<DisplayBean> getDatas() {

        List<DisplayBean> displayBeen = new ArrayList<>();

        if (false) {
            //如果没有聊天记录,那么就显示"暂无消息"
            IMEmptyViewData imEmptyViewData = new IMEmptyViewData();
            IMEmptyViewBean imEmptyViewBean = new IMEmptyViewBean(imEmptyViewData);
            displayBeen.add(imEmptyViewBean);

        } else {

            //时间
            RepairDetailTimeData repairDetailTimeData = new RepairDetailTimeData();
            repairDetailTimeData.setTime("昨天 18:01");
            RepairDetailTimeBean repairDetailTimeBean = new RepairDetailTimeBean(repairDetailTimeData);
            displayBeen.add(repairDetailTimeBean);

            for (int i = 0; i < 3; i++) {

                //物业聊天
                PropertyRepairDetailPropertyContentData propertyRepairDetailPropertyContentData = new PropertyRepairDetailPropertyContentData();
                propertyRepairDetailPropertyContentData.setResId(R.mipmap.ic_launcher);
                propertyRepairDetailPropertyContentData.setContent("钓鱼岛和南海是中国的!!!" + "我说了" + (i + 1) + "遍!!!" + (i == 1 ? "切负荷七分我付款和违法和文化符号" : ""));
                PropertyRepairDetailPropertyBean propertyRepairDetailPropertyBean = new PropertyRepairDetailPropertyBean(propertyRepairDetailPropertyContentData);
                displayBeen.add(propertyRepairDetailPropertyBean);

                //业主聊天
                PropertyRepairDetailProprietorContentData propertyRepairDetailProprietorContentData = new PropertyRepairDetailProprietorContentData();
                propertyRepairDetailProprietorContentData.setContent("美狗日杂些想作死就来");
                propertyRepairDetailProprietorContentData.setFailed(i == 2 ? false : true);
                PropertyRepairDetailProprietorContentBean propertyRepairDetailProprietorContentBean = new PropertyRepairDetailProprietorContentBean(propertyRepairDetailProprietorContentData);
                displayBeen.add(propertyRepairDetailProprietorContentBean);
            }

            //时间
            RepairDetailTimeData repairDetailTimeData1 = new RepairDetailTimeData();
            repairDetailTimeData1.setTime("今天 20:00");
            RepairDetailTimeBean repairDetailTimeBean1 = new RepairDetailTimeBean(repairDetailTimeData1);
            displayBeen.add(repairDetailTimeBean1);

            for (int j = 0; j < 2; j++) {
                //物业聊天
                PropertyRepairDetailPropertyContentData propertyRepairDetailPropertyContentData = new PropertyRepairDetailPropertyContentData();
                propertyRepairDetailPropertyContentData.setResId(R.mipmap.ic_launcher);
                propertyRepairDetailPropertyContentData.setContent("呵呵");
                PropertyRepairDetailPropertyBean propertyRepairDetailPropertyBean = new PropertyRepairDetailPropertyBean(propertyRepairDetailPropertyContentData);
                displayBeen.add(propertyRepairDetailPropertyBean);
            }

            //业主聊天
            PropertyRepairDetailProprietorContentData propertyRepairDetailProprietorContentData1 = new PropertyRepairDetailProprietorContentData();
            propertyRepairDetailProprietorContentData1.setContent("呵呵违法未发货我饿哦发货未缴费和违法违纪款翻倍卡进去分别为发卡号为范围哦回复");
            propertyRepairDetailProprietorContentData1.setFailed(true);
            PropertyRepairDetailProprietorContentBean propertyRepairDetailProprietorContentBean1 = new PropertyRepairDetailProprietorContentBean(propertyRepairDetailProprietorContentData1);
            displayBeen.add(propertyRepairDetailProprietorContentBean1);
        }
        return displayBeen;
    }

    public List<DisplayBean> getAlreadyChoosedPic() {

        List<DisplayBean> displayBeen = new ArrayList<>();

        // TODO: 16/7/28 新建Data和Bean以及ViewHolder,然后在这里拿到图片,然后去ViewHolder中使用Glide加载图片,然后去看看PhotoPicker内部跳转界面的实现,或者才用对话框创建一个界面来装个VP显示图片
        NewRepairDetailChoosedPicData newRepairDetailChoosedPicData = new NewRepairDetailChoosedPicData();
        newRepairDetailChoosedPicData.setPicUrl("http://h.hiphotos.baidu.com/baike/pic/item/6d81800a19d8bc3efeea65b98a8ba61ea9d345e6.jpg");
        NewRepairDetailChoosedPicBean newRepairDetailChoosedPicBean = new NewRepairDetailChoosedPicBean(newRepairDetailChoosedPicData);
        displayBeen.add(newRepairDetailChoosedPicBean);

        NewRepairDetailChoosedPicData newRepairDetailChoosedPicData1 = new NewRepairDetailChoosedPicData();
        newRepairDetailChoosedPicData1.setPicUrl("http://e.hiphotos.baidu.com/baike/pic/item/b999a9014c086e0659c4dc7507087bf40ad1cba4.jpg");
        NewRepairDetailChoosedPicBean newRepairDetailChoosedPicBean1 = new NewRepairDetailChoosedPicBean(newRepairDetailChoosedPicData1);
        displayBeen.add(newRepairDetailChoosedPicBean1);

        NewRepairDetailChoosedPicData newRepairDetailChoosedPicData2 = new NewRepairDetailChoosedPicData();
        newRepairDetailChoosedPicData2.setPicUrl("http://e.hiphotos.baidu.com/baike/pic/item/a5c27d1ed21b0ef4af04b077dec451da81cb3e2e.jpg");
        NewRepairDetailChoosedPicBean newRepairDetailChoosedPicBean2 = new NewRepairDetailChoosedPicBean(newRepairDetailChoosedPicData2);
        displayBeen.add(newRepairDetailChoosedPicBean2);

        return displayBeen;
    }
}
