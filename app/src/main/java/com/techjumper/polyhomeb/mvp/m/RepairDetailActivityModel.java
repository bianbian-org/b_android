package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailPropertyContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailProprietorContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.RepairDetailTimeData;
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

        return displayBeen;
    }
}
