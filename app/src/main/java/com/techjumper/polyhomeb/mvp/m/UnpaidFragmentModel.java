package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NoDataData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PaymentFragmentContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PaymentTitleData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NoDataBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PaymentFragmentContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PaymentTitleBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.entity.OrdersEntity;
import com.techjumper.polyhomeb.mvp.p.fragment.UnpaidFragmentPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UnpaidFragmentModel extends BaseModel<UnpaidFragmentPresenter> {

    private int mCurrentPage = 1;
    private int mOnePageCount = 8;
    public boolean mIsFirst = true;
    private List<DisplayBean> mDataList = new ArrayList<>();

    private static final String sStatus = "1";

    public UnpaidFragmentModel(UnpaidFragmentPresenter presenter) {
        super(presenter);
    }

    public Observable<OrdersEntity> getOrdersInfo(String payType) {
        KeyValuePair keyValuePair = KeyValueCreator.getOrdersInfo(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket()
                , getFamilyId()
                , getVillageId()
                , sStatus
                , payType
                , mCurrentPage
                , mOnePageCount);
        Map<String, String> map = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault().getOrdersInfo(map).compose(CommonWrap.wrap());
    }

    public boolean hasMoreData(OrdersEntity entity) {
        return !(entity == null || entity.getData() == null || entity.getData().getOrders() == null
                || entity.getData().getOrders().size() < mOnePageCount);
    }

    public void updateOrdersData(OrdersEntity entity) {
        if (entity == null || entity.getData() == null
                || entity.getData().getOrders() == null || entity.getData().getOrders().size() == 0)
            return;

        if (mCurrentPage == 1) {
            mDataList.clear();
        }

        double total_price = entity.getData().getTotal_price();
        List<OrdersEntity.DataBean.OrdersBean> orders = entity.getData().getOrders();

        //第0个item,大分割线
        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        mDataList.add(propertyRepairBigDividerBean);

        if (mIsFirst) {
            //第一个item,带总数量什么的
            PaymentTitleData paymentTitleData = new PaymentTitleData();
            paymentTitleData.setTitle(getPresenter().getView().getString(R.string.un_payment_total) + ":");
            paymentTitleData.setTotal(total_price);
            paymentTitleData.setWhere(Constant.UNPAID_FRAGMENT_TITLE);
            PaymentTitleBean paymentTitleBean = new PaymentTitleBean(paymentTitleData);
            mDataList.add(paymentTitleBean);

            //第二个item,长的分割线
            PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
            PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
            mDataList.add(propertyPlacardDividerLongBean);

            mIsFirst = false;
        }

        for (int i = 0; i < orders.size(); i++) {

            OrdersEntity.DataBean.OrdersBean ordersBean = orders.get(i);

            int expiry = ordersBean.getExpiry();                //#逾期时间（天）  +24意味着还有24天缴费,-24意味着已经逾期24天
            String expiry_date = ordersBean.getExpiry_date();   //#缴费日期  "2016-09-22"
            double expiry_price = ordersBean.getExpiry_price(); //#滞纳金   15.19
            String object = ordersBean.getObject();             //#缴费对象    "1栋3单元21-111"
            String order_number = ordersBean.getOrder_number(); //#订单号   "2014731502779997"
            String pay_name = ordersBean.getPay_name();         //#费用名称   "8月份电费"
            int pay_type = ordersBean.getPay_type();            //#缴费类型 1-物业费 2-水费 3-电费 4-燃气费 5-其他
            double price = ordersBean.getPrice();               //#缴费金额    96.68
            int status = ordersBean.getStatus();                //#缴费状态 1-未缴费 2-已缴费
            int is_late = ordersBean.getIs_late();              //#是否逾期 0-没逾期, 1-逾期

            //第三个item,内容
            PaymentFragmentContentData paymentFragmentContentData = new PaymentFragmentContentData();
            paymentFragmentContentData.setTitle(pay_name);
            paymentFragmentContentData.setBtnName(pay_type);
            paymentFragmentContentData.setTime(expiry_date);
            paymentFragmentContentData.setContent(object);
            paymentFragmentContentData.setDay(expiry);
            paymentFragmentContentData.setPrice(price);
            paymentFragmentContentData.setExpiry_price(expiry_price);
            paymentFragmentContentData.setOrder_num(order_number);
            paymentFragmentContentData.setStatus(status);
            paymentFragmentContentData.setIs_late(is_late);
            PaymentFragmentContentBean paymentFragmentContentBean = new PaymentFragmentContentBean(paymentFragmentContentData);
            mDataList.add(paymentFragmentContentBean);

            if (i != orders.size() - 1)
                mDataList.add(propertyRepairBigDividerBean);
        }

        if (hasMoreData(entity)) {
            increasePage();
        }
    }

    //无数据的时候显示的视图
    public List<DisplayBean> noData() {
        List<DisplayBean> displayBeen = new ArrayList<>();

        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        displayBeen.add(propertyRepairBigDividerBean);

        PaymentTitleData paymentTitleData = new PaymentTitleData();
        paymentTitleData.setTitle(getPresenter().getView().getString(R.string.un_payment_total) + ":");
        paymentTitleData.setTotal(0);
        paymentTitleData.setWhere(Constant.UNPAID_FRAGMENT_TITLE);
        PaymentTitleBean paymentTitleBean = new PaymentTitleBean(paymentTitleData);
        displayBeen.add(paymentTitleBean);

        NoDataData noDataData = new NoDataData();
        NoDataBean noDataBean = new NoDataBean(noDataData);
        displayBeen.add(noDataBean);
        return displayBeen;
    }

    public List<DisplayBean> getOrdersData() {
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

    private String getFamilyId() {
        if (UserManager.INSTANCE.isFamily()) {
            return UserManager.INSTANCE.getCurrentId();
        } else {
            return "";
        }
    }

    private String getVillageId() {
        return UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID);
    }

}
