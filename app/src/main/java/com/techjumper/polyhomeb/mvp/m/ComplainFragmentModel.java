package com.techjumper.polyhomeb.mvp.m;

import android.text.TextUtils;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NoDataData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyComplainTitleData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairPropertyResponseData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NoDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyComplainTitleBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairPropertyResponseBean;
import com.techjumper.polyhomeb.entity.PropertyComplainEntity;
import com.techjumper.polyhomeb.mvp.p.fragment.ComplainFragmentPresenter;
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
 * Date: 16/7/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ComplainFragmentModel extends BaseModel<ComplainFragmentPresenter> {

    private int mCurrentPage = 1;
    private int mOnePageCount = 8;
    private List<DisplayBean> mDataList = new ArrayList<>();

    public boolean mIsFirst = true;

    public ComplainFragmentModel(ComplainFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<PropertyComplainEntity> getComplain(String status) {
        KeyValuePair keyValuePair = KeyValueCreator.propertyComplain(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , status
                , mCurrentPage + ""
                , mOnePageCount + "");
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .propertyComplain(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }

    public boolean hasMoreData(PropertyComplainEntity entity) {
        return !(entity == null || entity.getData() == null || entity.getData().getSuggestions() == null
                || entity.getData().getSuggestions().size() < mOnePageCount);
    }

    public void updateComplainData(PropertyComplainEntity entity) {
        if (entity == null || entity.getData() == null
                || entity.getData().getSuggestions() == null || entity.getData().getSuggestions().size() == 0)
            return;

        if (mCurrentPage == 1) {
            mDataList.clear();
        }

        PropertyComplainEntity.DataBean dataBean = entity.getData();
        List<PropertyComplainEntity.DataBean.SuggestionsBean> suggestions = dataBean.getSuggestions();

        //第0个item, 大分割线
        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        mDataList.add(propertyRepairBigDividerBean);

        if (mIsFirst) {
            //第一个item,带总数量什么的
            PropertyComplainTitleData propertyComplainTitleData = new PropertyComplainTitleData();
            propertyComplainTitleData.setTitle(getPresenter().getView().getString(R.string.complain_header_title));
            propertyComplainTitleData.setCount(entity.getData().getCount());
            PropertyComplainTitleBean propertyComplainTitleBean = new PropertyComplainTitleBean(propertyComplainTitleData);
            mDataList.add(propertyComplainTitleBean);

            //第二个item,长的分割线
            PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
            PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
            mDataList.add(propertyPlacardDividerLongBean);

            mIsFirst = false;
        }

        for (int i = 0; i < suggestions.size(); i++) {

            PropertyComplainEntity.DataBean.SuggestionsBean suggestionsBean = suggestions.get(i);

            String content = suggestionsBean.getContent();   //内容
            String created_at = suggestionsBean.getCreated_at();  //时间 1470304135
            int id = suggestionsBean.getId();   //对应消息的id
            String replies = suggestionsBean.getReplies();   //物业回复,有就显示,没有就不显示
            int status = suggestionsBean.getStatus();  //0-未处理 1-已回复 2-已处理 3-已关闭
            int types = suggestionsBean.getTypes();  //int型的标题  1-投诉 2-建议 3-表扬
            int user_id = suggestionsBean.getUser_id();  //user_id
            String user_name = suggestionsBean.getUser_name();  //user_name
            String btnName = "";
            switch (status) {
                case 0:
                    btnName = getPresenter().getView().getString(R.string.pop_not_process); //未处理
                    break;
                case 1:
                    btnName = getPresenter().getView().getString(R.string.pop_reply);  //已回复
                    break;
                case 2:
                    btnName = getPresenter().getView().getString(R.string.pop_processed);  //已处理
                    break;
                case 3:
                    btnName = getPresenter().getView().getString(R.string.pop_closed);  //已关闭
                    break;
            }
            String title = "";
            switch (types) {
                case 1:
                    title = getPresenter().getView().getString(R.string.complain);
                    break;
                case 2:
                    title = getPresenter().getView().getString(R.string.advice);
                    break;
                case 3:
                    title = getPresenter().getView().getString(R.string.celebrate);
                    break;
            }

            long time_ = Long.parseLong(created_at);
            SimpleDateFormat format = new SimpleDateFormat(getPresenter().getView().getResources().getString(R.string.pattren_M_D));
            String time = format.format(new Date(time_ * 1000));

            //第三个item,内容
            PropertyRepairContentData propertyRepairContentData = new PropertyRepairContentData();
            propertyRepairContentData.setTitle(title);
            propertyRepairContentData.setRead(false);
            propertyRepairContentData.setUser_id(user_id);
            propertyRepairContentData.setBtnName(btnName);
            propertyRepairContentData.setUser_name(user_name);
            propertyRepairContentData.setStatus(status);
            propertyRepairContentData.setCreate_time(created_at);
            propertyRepairContentData.setTime(time);
            propertyRepairContentData.setContent(content);
            propertyRepairContentData.setId(id);
            propertyRepairContentData.setTypes(types);
            PropertyRepairContentBean propertyRepairContentBean = new PropertyRepairContentBean(propertyRepairContentData);
            mDataList.add(propertyRepairContentBean);

            if (TextUtils.isEmpty(replies)) {
                //如果物业回复是空的,那么直接加一个大的分割线
                //如果当前的listBean是总数据的最后一个,那么就不能加大的分割线了
                if (i != suggestions.size() - 1) {
                    mDataList.add(propertyRepairBigDividerBean);
                }
            } else {
                //第四个item,短点的分割线
                PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                mDataList.add(dividerBean);
                //第五个item,物业回复
                PropertyRepairPropertyResponseData propertyRepairPropertyResponseData = new PropertyRepairPropertyResponseData();
                propertyRepairPropertyResponseData.setResponse(replies);
                PropertyRepairPropertyResponseBean propertyRepairPropertyResponseBean = new PropertyRepairPropertyResponseBean(propertyRepairPropertyResponseData);
                mDataList.add(propertyRepairPropertyResponseBean);
                //如果物业回复不是空的话,那么在短点的分割线以及物业回复之后再加大的分割线
                //如果当前的listBean是总数据的最后一个,那么就不能加大的分割线了
                if (i != suggestions.size() - 1) {
                    mDataList.add(propertyRepairBigDividerBean);
                }
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
        NoDataData noDataData = new NoDataData();
        NoDataBean noDataBean = new NoDataBean(noDataData);
        displayBeen.add(noDataBean);
        return displayBeen;
    }

    public List<DisplayBean> getComplainData() {
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
