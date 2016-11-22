package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.Constant;
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
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.PropertyComplainDetailEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.mvp.p.activity.ComplainDetailActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ComplainDetailActivityModel extends BaseModel<ComplainDetailActivityPresenter> {

    private ArrayList<String> mPics = new ArrayList<>();
    private List<DisplayBean> mReplyDatas = new ArrayList<>();

    public ComplainDetailActivityModel(ComplainDetailActivityPresenter presenter) {
        super(presenter);
    }

    private int getDataId() {
        return getPresenter().getView().getIntent().getExtras().getInt(Constant.PROPERTY_COMPLAIN_DATA_ID, -1);
    }

    public int getMessageId() {
        return getPresenter().getView().getIntent().getExtras().getInt(Constant.KEY_MESSAGE_ID, 0);
    }

    public String getTypes(int types) {
        switch (types) {//1-投诉 2-建议 3-表扬
            case 1:
                return getPresenter().getView().getResources().getString(R.string.complain);
            case 2:
                return getPresenter().getView().getResources().getString(R.string.advice);
            case 3:
                return getPresenter().getView().getResources().getString(R.string.celebrate);
            default:
                return getPresenter().getView().getResources().getString(R.string.no_title);
        }
    }

    public String getMonthAndDayTime(String time) {
        long time_ = Long.parseLong(time);
        SimpleDateFormat format = new SimpleDateFormat(getPresenter().getView().getResources().getString(R.string.pattren_M_D));
        return format.format(new Date(time_ * 1000));
    }

    public String getStatusName(int status) {
        //"status": 0,  #状态  0-未处理 1-已回复 2-已处理 3-已关闭
        switch (status) {
            case 0:
                return getPresenter().getView().getString(R.string.pop_not_process);
            case 1:
                return getPresenter().getView().getString(R.string.pop_reply);
            case 2:
                return getPresenter().getView().getString(R.string.pop_processed);
            case 3:
                return getPresenter().getView().getString(R.string.pop_closed);
            default:
                return getPresenter().getView().getString(R.string.no_title);
        }
    }

    public List<DisplayBean> getAlreadyChoosedPic(PropertyComplainDetailEntity propertyComplainDetailEntity) {
        String[] imgs = propertyComplainDetailEntity.getData().getImgs();
        if (imgs != null && imgs.length != 0) {
            mPics.clear();
            for (int i = 0; i < imgs.length; i++) {
                String img = imgs[i];
                mPics.add(Config.sHost + img);
            }
        }
        List<DisplayBean> displayBeen = new ArrayList<>();
        for (String url : mPics) {
            NewRepairDetailChoosedPicData newRepairDetailChoosedPicData = new NewRepairDetailChoosedPicData();
            newRepairDetailChoosedPicData.setPicUrl(url);
            NewRepairDetailChoosedPicBean newRepairDetailChoosedPicBean = new NewRepairDetailChoosedPicBean(newRepairDetailChoosedPicData);
            displayBeen.add(newRepairDetailChoosedPicBean);
        }
        return displayBeen;
    }

    public Observable<PropertyComplainDetailEntity> getComplainDetail() {
        KeyValuePair keyValuePair = KeyValueCreator.getComplainDetail(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , getDataId());
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getComplainDetail(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }

    public ArrayList<String> getPics() {
        return mPics;
    }


    /*----------------------------------------聊天-----------------------------------------*/

    public Observable<TrueEntity> complainDetailReply(String content) {
        KeyValuePair keyValuePair = KeyValueCreator.complainDetailReply(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , content
                , getDataId() + "");
        BaseArgumentsEntity baseArgumentsEntity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .complainDetailReply(baseArgumentsEntity)
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> getEmptyData() {
        //如果没有聊天记录,那么就显示"暂无消息"
        List<DisplayBean> displayBeen = new ArrayList<>();
        IMEmptyViewData imEmptyViewData = new IMEmptyViewData();
        IMEmptyViewBean imEmptyViewBean = new IMEmptyViewBean(imEmptyViewData);
        displayBeen.add(imEmptyViewBean);
        return displayBeen;
    }

    public List<DisplayBean> getReplyDatas(List<PropertyComplainDetailEntity.DataBean.RepliesBean> replies) {

        Collections.reverse(replies);
        List<DisplayBean> displayBeen = new ArrayList<>();
        int this_user_id = Integer.parseInt(UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID));

        for (int i = 0; i < replies.size(); i++) {

            int user_id = replies.get(i).getUser_id();

            String time = replies.get(i).getTime();

            if (this_user_id == user_id) {

                //时间
                RepairDetailTimeData repairDetailTimeData = new RepairDetailTimeData();
                repairDetailTimeData.setTime(time);
                RepairDetailTimeBean repairDetailTimeBean = new RepairDetailTimeBean(repairDetailTimeData);
                displayBeen.add(repairDetailTimeBean);

                //业主聊天
                PropertyRepairDetailProprietorContentData propertyRepairDetailProprietorContentData = new PropertyRepairDetailProprietorContentData();
                propertyRepairDetailProprietorContentData.setContent(replies.get(i).getContent());
                propertyRepairDetailProprietorContentData.setSendStatus(Constant.MESSAGE_SEND_SUCCESS);
                PropertyRepairDetailProprietorContentBean propertyRepairDetailProprietorContentBean = new PropertyRepairDetailProprietorContentBean(propertyRepairDetailProprietorContentData);
                displayBeen.add(propertyRepairDetailProprietorContentBean);
            } else {

                //时间
                RepairDetailTimeData repairDetailTimeData = new RepairDetailTimeData();
                repairDetailTimeData.setTime(time);
                RepairDetailTimeBean repairDetailTimeBean = new RepairDetailTimeBean(repairDetailTimeData);
                displayBeen.add(repairDetailTimeBean);

                //物业聊天
                PropertyRepairDetailPropertyContentData propertyRepairDetailPropertyContentData = new PropertyRepairDetailPropertyContentData();
                propertyRepairDetailPropertyContentData.setResId(R.mipmap.icon_property_avatar);
                propertyRepairDetailPropertyContentData.setContent(replies.get(i).getContent());
                PropertyRepairDetailPropertyBean propertyRepairDetailPropertyBean = new PropertyRepairDetailPropertyBean(propertyRepairDetailPropertyContentData);
                displayBeen.add(propertyRepairDetailPropertyBean);
            }
        }
        mReplyDatas.addAll(displayBeen);
        return displayBeen;
    }

    public List<DisplayBean> getReplyDatas() {
        return mReplyDatas;
    }

    public Observable<TrueEntity> updateMessage() {
        KeyValuePair keyValuePair = KeyValueCreator.updateMessageState(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , getMessageId() + "");
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault().updateMessageState(entity)
                .compose(CommonWrap.wrap());

    }

}
