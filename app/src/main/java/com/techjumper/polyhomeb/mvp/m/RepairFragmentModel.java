package com.techjumper.polyhomeb.mvp.m;

import android.text.TextUtils;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NoDataData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairPropertyResponseData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairTitleData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NoDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairPropertyResponseBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairTitleBean;
import com.techjumper.polyhomeb.entity.PropertyRepairEntity;
import com.techjumper.polyhomeb.mvp.p.fragment.RepairFragmentPresenter;
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
 * Date: 16/7/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RepairFragmentModel extends BaseModel<RepairFragmentPresenter> {

    private int mCurrentPage = 1;
    private int mOnePageCount = 8;
    private List<DisplayBean> mDataList = new ArrayList<>();

    public boolean mIsFirst = true;

    public RepairFragmentModel(RepairFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<PropertyRepairEntity> getRepair(String status) {
        KeyValuePair keyValuePair = KeyValueCreator.propertyRepair(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , status
                , UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID)
                , mCurrentPage + ""
                , mOnePageCount + "");
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .propertyRepair(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }

    public boolean hasMoreData(PropertyRepairEntity entity) {
        return !(entity == null || entity.getData() == null || entity.getData().getRepairs() == null
                || entity.getData().getRepairs().size() < mOnePageCount);
    }

    public void updateRepairData(PropertyRepairEntity entity) {

        if (entity == null || entity.getData() == null
                || entity.getData().getRepairs() == null || entity.getData().getRepairs().size() == 0)
            return;

        if (mCurrentPage == 1) {
            mDataList.clear();
        }

        PropertyRepairEntity.DataBean dataBean = entity.getData();
        List<PropertyRepairEntity.DataBean.RepairsBean> repairs = dataBean.getRepairs();

        //第0个item,大分割线
        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        mDataList.add(propertyRepairBigDividerBean);

        if (mIsFirst) {
            //第一个item,带总数量什么的
            PropertyRepairTitleData propertyRepairTitleData = new PropertyRepairTitleData();
            propertyRepairTitleData.setTitle(getPresenter().getView().getResources().getString(R.string.repair_header_title));
            propertyRepairTitleData.setCount(entity.getData().getCount());
            PropertyRepairTitleBean propertyRepairTitleBean = new PropertyRepairTitleBean(propertyRepairTitleData);
            mDataList.add(propertyRepairTitleBean);

            //第二个item,长的分割线
            PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
            PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
            mDataList.add(propertyPlacardDividerLongBean);

            mIsFirst = false;
        }

        for (int i = 0; i < repairs.size(); i++) {

            PropertyRepairEntity.DataBean.RepairsBean repairsBean = repairs.get(i);

            String content = repairsBean.getNote();  //内容
            String created_at = repairsBean.getRepair_date();  //时间 1470301482
            int id = repairsBean.getId();  //对应消息的id
            int user_id = repairsBean.getUser_id(); //user_id
            String user_name = repairsBean.getUser_name(); //user_name
            String replies = repairsBean.getReplies();  //物业回复,有就显示,没有就不显示
            int status = repairsBean.getStatus();  //0-未处理 1-已回复 2-已处理 3-已关闭
            /*标题*/
            int repair_type = repairsBean.getRepair_type();  //报修类型 1-个人报修 2-公共区域报修
            int repair_device = repairsBean.getRepair_device();  //报修设备 1-门窗类 2-水电类 3-锁类 4-电梯类
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
            String type = "";
            String device = "";
            switch (repair_type) {
                case 1:
                    type = getPresenter().getView().getString(R.string.pop_personal);  //个人
                    break;
                case 2:
                    type = getPresenter().getView().getString(R.string.pop_common);  //公共
                    break;
            }
            switch (repair_device) {
                case 1:
                    device = getPresenter().getView().getString(R.string.pop_windows); //门窗类
                    break;
                case 2:
                    device = getPresenter().getView().getString(R.string.pop_water_elec); //水电类
                    break;
                case 3:
                    device = getPresenter().getView().getString(R.string.pop_locks); //锁类
                    break;
                case 4:
                    device = getPresenter().getView().getString(R.string.pop_elevators); //电梯类
                    break;
            }
            title = type + (getPresenter().getView().getString(R.string.repair_)) + "-" + device;

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
            propertyRepairContentData.setRepair_type(repair_type);
            propertyRepairContentData.setRepair_device(repair_device);
            PropertyRepairContentBean propertyRepairContentBean = new PropertyRepairContentBean(propertyRepairContentData);
            mDataList.add(propertyRepairContentBean);

            if (TextUtils.isEmpty(replies)) {
                //如果物业回复是空的,那么直接加一个大的分割线
                //如果当前的listBean是总数据的最后一个,那么就不能加大的分割线了
                if (i != repairs.size() - 1) {
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
                if (i != repairs.size() - 1) {
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

        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        displayBeen.add(propertyRepairBigDividerBean);

        PropertyRepairTitleData propertyRepairTitleData = new PropertyRepairTitleData();
        propertyRepairTitleData.setTitle(getPresenter().getView().getResources().getString(R.string.repair_header_title));
        propertyRepairTitleData.setCount(0);
        PropertyRepairTitleBean propertyRepairTitleBean = new PropertyRepairTitleBean(propertyRepairTitleData);
        displayBeen.add(propertyRepairTitleBean);

        NoDataData noDataData = new NoDataData();
        NoDataBean noDataBean = new NoDataBean(noDataData);
        displayBeen.add(noDataBean);
        return displayBeen;
    }

    public List<DisplayBean> getRepairData() {
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
