package com.techjumper.polyhomeb.mvp.m;

import android.text.Html;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NoDataData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardTimeLineData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NoDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardTimeLineBean;
import com.techjumper.polyhomeb.entity.PropertyPlacardEntity;
import com.techjumper.polyhomeb.mvp.p.fragment.PlacardFragmentPresenter;
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
 * Date: 16/7/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PlacardFragmentModel extends BaseModel<PlacardFragmentPresenter> {

    private String lastMonth = "";
    private int mCurrentPage = 1;
    private int mOnePageCount = 8;
    private List<DisplayBean> mDataList = new ArrayList<>();

    public PlacardFragmentModel(PlacardFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<PropertyPlacardEntity> getNotice() {
        boolean family = UserManager.INSTANCE.isFamily();
        String family_id;
        String village_id;
        if (family) {
            family_id = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_ID);
            village_id = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID);
        } else {
            village_id = UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID);
            family_id = "";
        }
        KeyValuePair keyValuePair = KeyValueCreator.propertyNotice(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , village_id
                , family_id
                , UserManager.INSTANCE.getTicket()
                , mCurrentPage + ""
                , mOnePageCount + "");
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .propertyNotice(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }

    public boolean hasMoreData(PropertyPlacardEntity entity) {
        return !(entity == null || entity.getData() == null || entity.getData().getNotices() == null
                || entity.getData().getNotices().size() < mOnePageCount);
    }

    public void updateNoticeData(PropertyPlacardEntity entity) {

        if (entity == null || entity.getData() == null
                || entity.getData().getNotices() == null || entity.getData().getNotices().size() == 0)
            return;

        if (mCurrentPage == 1) {
            mDataList.clear();
        }

        PropertyPlacardEntity.DataBean dataBean = entity.getData();
        List<PropertyPlacardEntity.DataBean.NoticesBean> notices = dataBean.getNotices();

        //只有一条数据的时候
        if (notices.size() == 1) {
            int id = notices.get(0).getId();
            String content = notices.get(0).getContent();
            String timeLong = notices.get(0).getTime();
            String title = notices.get(0).getTitle();
            int types = notices.get(0).getTypes();
            String type = types == 1 ? getPresenter().getView().getString(R.string.placard) : getPresenter().getView().getString(R.string.information);//#公告类型 1-公告 2-资讯

            long time_ = Long.parseLong(timeLong);
            SimpleDateFormat format = new SimpleDateFormat(getPresenter().getView().getResources().getString(R.string.pattren_M_D));
            String time = format.format(new Date(time_ * 1000));

            //这个判断，主要是为了针对  第一页8条数据，第二页只有一条的情况
            //如果不加判断，那么第一页8条数据完毕后，如果第二页只有一条，并且第二页的数据刚好和第一页结尾的数据 月份一样
            //那么依然会增加 时间轴，所以这里多加个判断。
            if (!lastMonth.equals(new SimpleDateFormat("M").format(new Date(time_ * 1000)))) {//就说明第一个时间区域完结,此时布局需要加载新的时间轴title

                //绿色的 12月 那个时间轴
                PropertyPlacardTimeLineData timeLineData = new PropertyPlacardTimeLineData();
                timeLineData.setTime(new SimpleDateFormat("M").format(new Date(time_ * 1000)) + getPresenter().getView().getString(R.string.month));
                PropertyPlacardTimeLineBean timeLineBean = new PropertyPlacardTimeLineBean(timeLineData);
                mDataList.add(timeLineBean);

                //中间的item
                addItem(title, type, time, content, id);

            } else {//就说明第一个时间区域还有数据,需要继续走,此时布局只是连续加载分割线和item

                //短一点的分割线
                PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                mDataList.add(dividerBean);

                //中间的item
                addItem(title, type, time, content, id);
            }
        } else {
            //两条数据或者多条数据的时候
            for (int i = 0; i < notices.size(); i++) {

                PropertyPlacardEntity.DataBean.NoticesBean noticesBean = notices.get(i);

                String content = noticesBean.getContent();
                int id = noticesBean.getId();
                String timeLong = noticesBean.getTime();
                String title = noticesBean.getTitle();
                int types = noticesBean.getTypes();
                String type = types == 1 ? getPresenter().getView().getString(R.string.placard) : getPresenter().getView().getString(R.string.information);//#公告类型 1-公告 2-资讯

                long time_ = Long.parseLong(timeLong);
                SimpleDateFormat format = new SimpleDateFormat(getPresenter().getView().getResources().getString(R.string.pattren_M_D));
                String time = format.format(new Date(time_ * 1000));

                if (!lastMonth.equals(new SimpleDateFormat("M").format(new Date(time_ * 1000)))) {//就说明第一个时间区域完结,此时布局需要加载新的时间轴title

                    //绿色的 12月 那个时间轴
                    PropertyPlacardTimeLineData timeLineData = new PropertyPlacardTimeLineData();
                    timeLineData.setTime(new SimpleDateFormat("M").format(new Date(time_ * 1000)) + getPresenter().getView().getString(R.string.month));
                    PropertyPlacardTimeLineBean timeLineBean = new PropertyPlacardTimeLineBean(timeLineData);
                    mDataList.add(timeLineBean);

                    //中间的item
                    addItem(title, type, time, content, id);

                } else {//就说明第一个时间区域还有数据,需要继续走,此时布局只是连续加载分割线和item

                    //短一点的分割线
                    PropertyPlacardDividerData dividerData = new PropertyPlacardDividerData();
                    PropertyPlacardDividerBean dividerBean = new PropertyPlacardDividerBean(dividerData);
                    mDataList.add(dividerBean);

                    //中间的item
                    addItem(title, type, time, content, id);
                }
                lastMonth = new SimpleDateFormat("M").format(new Date(time_ * 1000));
            }
        }

        if (hasMoreData(entity)) {
            increasePage();
        }
    }

    private void addItem(String title, String type, String time, String content, int id) {
        //中间的item
        PropertyPlacardContentData propertyPlacardContentData = new PropertyPlacardContentData();
        propertyPlacardContentData.setTitle(title);
        propertyPlacardContentData.setRead(false);
        propertyPlacardContentData.setType(type);
        propertyPlacardContentData.setTime(time);
        propertyPlacardContentData.setContent(replaceWebTag(content));
        propertyPlacardContentData.setContent_(content);
        propertyPlacardContentData.setId(id);
        PropertyPlacardContentBean contentBean = new PropertyPlacardContentBean(propertyPlacardContentData);
        mDataList.add(contentBean);
    }

    //无数据的时候显示的视图
    public List<DisplayBean> noData() {
        List<DisplayBean> displayBeen = new ArrayList<>();
        NoDataData noDataData = new NoDataData();
        NoDataBean noDataBean = new NoDataBean(noDataData);
        displayBeen.add(noDataBean);
        return displayBeen;
    }

    public List<DisplayBean> getNoticeData() {
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

    private String replaceWebTag(String webContent) {
//        String content = webContent
//                .replace("<p>", "")
//                .replace("<br>", "")
//                .replace("</p>", "")
//                .replace("</br>", "");
//        return content;
        return Html.fromHtml(webContent).toString();
    }
}
