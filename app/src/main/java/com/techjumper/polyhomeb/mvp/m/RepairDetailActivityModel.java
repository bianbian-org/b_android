package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailPropertyContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailProprietorContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailStaticData;
import com.techjumper.polyhomeb.adapter.recycler_Data.RepairDetailTimeData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairDetailPropertyBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairDetailProprietorContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairDetailStaticBean;
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

        PropertyRepairDetailStaticData propertyRepairDetailStaticData = new PropertyRepairDetailStaticData();
        propertyRepairDetailStaticData.setTime("12月22日");
        propertyRepairDetailStaticData.setTitle("个人报修-门窗");
        propertyRepairDetailStaticData.setContent("让我返回为归结为efefefefefeefefefefefefefefefefefefefefefefefefef攻击我配个为非法违法而非任务范围分为访问服务范围而外围绯闻绯闻绯闻金额为快乐女接wefefwejfjwefwefewfewfwefwefewfwefwekwejf而分为of问佛我威锋网付金额为 去看附件二起来可访问附件为浪费金额为浪费金额为放假前为了减肥我放进去了二级网 吻挎包vwekgwekbgwelkbgweklnvwkefhqpofhwev   而无法为库房内为开发快忘了发");
        PropertyRepairDetailStaticBean propertyRepairDetailStaticBean = new PropertyRepairDetailStaticBean(propertyRepairDetailStaticData);
        displayBeen.add(propertyRepairDetailStaticBean);

        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        displayBeen.add(propertyRepairBigDividerBean);

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
