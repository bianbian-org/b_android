package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MessageAllContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.MessageBigTitleData;
import com.techjumper.polyhomeb.adapter.recycler_Data.NoDataData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MessageAllContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MessageBigTitleBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NoDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.entity.MessageEntity;
import com.techjumper.polyhomeb.mvp.p.fragment.MessageOrdersFragmentPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MessageOrdersFragmentModel extends BaseModel<MessageOrdersFragmentPresenter> {

    private int mCurrentPage = 1;
    private int mOnePageCount = 8;
    private List<DisplayBean> mDataList = new ArrayList<>();
    public boolean mIsFirst = true;

    public MessageOrdersFragmentModel(MessageOrdersFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<MessageEntity> getMessages() {
        KeyValuePair keyValuePair = KeyValueCreator.getMessages(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , "2"  //传入2,代表请求订单消息
                , mCurrentPage
                , mOnePageCount);
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getMessages(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }

    public boolean hasMoreData(MessageEntity entity) {
        return !(entity == null || entity.getData() == null || entity.getData().getResult() == null
                || entity.getData().getResult().getMessages() == null
                || entity.getData().getResult().getMessages().size() < mOnePageCount);
    }

    public void updateData(MessageEntity entity) {

        if (entity == null || entity.getData() == null
                || entity.getData().getResult().getMessages() == null || entity.getData().getResult().getMessages().size() == 0)
            return;

        if (mCurrentPage == 1) {
            mDataList.clear();
        }

        MessageEntity.DataBean dataBean = entity.getData();
        MessageEntity.DataBean.ResultBean result = dataBean.getResult();
        List<MessageEntity.DataBean.ResultBean.MessagesBean> messages = result.getMessages();

        //第0个item,大分割线
        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        mDataList.add(propertyRepairBigDividerBean);

        if (mIsFirst) {
            //第一个item,带总数量什么的
            MessageBigTitleData messageBigTitleData = new MessageBigTitleData();
            messageBigTitleData.setTitle_name(getPresenter().getView().getResources().getString(R.string.message_order));
            messageBigTitleData.setTotal_num(entity.getData().getResult().getAll_count());
            MessageBigTitleBean messageBigTitleBean = new MessageBigTitleBean(messageBigTitleData);
            mDataList.add(messageBigTitleBean);

            //第二个item,长的分割线
            PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
            PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
            mDataList.add(propertyPlacardDividerLongBean);

            mIsFirst = false;
        }

        for (int i = 0; i < messages.size(); i++) {

            MessageEntity.DataBean.ResultBean.MessagesBean messagesBean = messages.get(i);

            String content = messagesBean.getContent();   //消息内容
            String created_at = messagesBean.getCreated_at();  //1467254681 创建时间
            int id = messagesBean.getId();              //消息id
            String obj_id = messagesBean.getObj_id();  //#对象ID，如订单ID等
            String title = messagesBean.getTitle();  //消息title
            String rightText = messagesBean.getTypes(); // #消息类型 1-系统信息 2-订单信息 4，5-物业信息 6-友邻
            long time_ = Long.parseLong(created_at);
            SimpleDateFormat format = new SimpleDateFormat(getPresenter().getView().getResources().getString(R.string.pattren_M_D));
            String time = format.format(new Date(time_ * 1000));  //转换后的时间,只剩下月和日

            switch (Integer.parseInt(rightText)) {
                case 1:
                    rightText = getPresenter().getView().getString(R.string.message_title_system);
                    break;
                case 2:
                    rightText = getPresenter().getView().getString(R.string.message_title_orders);
                    break;
                case 4:
                case 5:
                    rightText = getPresenter().getView().getString(R.string.message_title_property);
                    break;
                case 6:
                    rightText = getPresenter().getView().getString(R.string.message_title_friend);
                    break;
            }
            //第三个item,内容
            MessageAllContentData messageAllContentData = new MessageAllContentData();
            messageAllContentData.setContent(content);
            messageAllContentData.setRightText(rightText);
            messageAllContentData.setTime(time);
            messageAllContentData.setTitle(title);
            messageAllContentData.setId(id);
            messageAllContentData.setObj_id(obj_id);
            MessageAllContentBean messageAllContentBean = new MessageAllContentBean(messageAllContentData);
            mDataList.add(messageAllContentBean);

            //如果物业回复不是空的话,那么在短点的分割线以及物业回复之后再加大的分割线
            //如果当前的listBean是总数据的最后一个,那么就不能加大的分割线了
            if (i != messages.size() - 1) {
                mDataList.add(propertyRepairBigDividerBean);
            }
        }

        if (hasMoreData(entity)) {
            increasePage();
        }

    }

    /**
     * 无数据的时候显示的视图
     */
    public List<DisplayBean> noData() {
        List<DisplayBean> displayBeen = new ArrayList<>();

        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        displayBeen.add(propertyRepairBigDividerBean);

        MessageBigTitleData messageBigTitleData = new MessageBigTitleData();
        messageBigTitleData.setTitle_name(getPresenter().getView().getResources().getString(R.string.message_order));
        messageBigTitleData.setTotal_num(0);
        MessageBigTitleBean messageBigTitleBean = new MessageBigTitleBean(messageBigTitleData);
        displayBeen.add(messageBigTitleBean);

        NoDataData noDataData = new NoDataData();
        NoDataBean noDataBean = new NoDataBean(noDataData);
        displayBeen.add(noDataBean);
        return displayBeen;
    }

    public List<DisplayBean> getData() {
        return mDataList;
    }

    private void increasePage() {
        mCurrentPage++;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int page) {
        mCurrentPage = page;
    }
}
