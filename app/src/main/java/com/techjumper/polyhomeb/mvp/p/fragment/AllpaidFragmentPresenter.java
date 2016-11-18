package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.OrdersEntity;
import com.techjumper.polyhomeb.entity.PaymentTypeEntity;
import com.techjumper.polyhomeb.entity.event.PaymentQueryEvent;
import com.techjumper.polyhomeb.entity.event.RefreshPaymentEvent;
import com.techjumper.polyhomeb.mvp.m.AllpaidFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.AllpaidFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AllpaidFragmentPresenter extends AppBaseFragmentPresenter<AllpaidFragment> {

    public AllpaidFragmentModel mModel = new AllpaidFragmentModel(this);

    private Subscription mSubs1, mSubs2, mSubs3,mSubs5;
    private String mPayType = "";
    private String mTypeName = "";

    private List<PaymentTypeEntity.DataBean.ItemsBean> items = new ArrayList<>();

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getView().showLoading();
        getPayType();
        refreshPaymentData();
        refreshData();
    }

    //在付款成功界面,点击大的绿色返回按钮的时候,收到消息,刷新数据
    private void refreshPaymentData() {
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = RxBus.INSTANCE
                        .asObservable().subscribe(o -> {
                            if (o instanceof RefreshPaymentEvent) {
                                refreshData();
                            }
                        }));
    }

    private void getPayType() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof PaymentQueryEvent) {
                        PaymentQueryEvent event = (PaymentQueryEvent) o;
                        if (Constant.PAID_FRAGMENT_TITLE == event.getWhere()) {
                            //由于未付款和已付款用的是同一个title,所以需要根据这个字段来判断,Rx是从哪个title发出来的
                            if (event.getPosition() == -1) {
                                mPayType = "";
                            } else {
                                mPayType = event.getPosition() + "";
                            }
                            mTypeName = event.getTypeName();
                            refreshData();
                        }
                    }
                }));
    }

//    public void getOrdersInfo() {
//        RxUtils.unsubscribeIfNotNull(mSubs1);
//        addSubscription(
//                mSubs1 = mModel.getOrdersInfo(mPayType)
//                        .subscribe(new Subscriber<OrdersEntity>() {
//                            @Override
//                            public void onCompleted() {
//                                getView().dismissLoading();
//                                getView().stopRefresh("");
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                getView().dismissLoading();
//                                getView().showError(e);
//                                loadMoreError();
//                                getView().onOrdersDataReceive(mModel.noData());
//                                getView().stopRefresh("");
//                            }
//
//                            @Override
//                            public void onNext(OrdersEntity entity) {
////                                if (!processNetworkResult(entity)) {
////                                    return;
////                                }
////                                if (mModel.getCurrentPage() == 1 && entity.getData().getOrders().size() != 0) {
////                                    getView().setHasMoreData(true);
////                                }
////                                boolean hasMoreData = mModel.hasMoreData(entity);
////                                getView().setHasMoreData(hasMoreData);
////
////                                if (!hasMoreData && mModel.getCurrentPage() == 1) {
////                                    getView().onOrdersDataReceive(mModel.noData());
////                                }
////                                mModel.updateOrdersData(entity);
////                                getView().onOrdersDataReceive(mModel.getOrdersData());
//                                if (!processNetworkResult(entity)) {
//                                    return;
//                                }
//                                if (mModel.getCurrentPage() == 1 && entity.getData().getOrders().size() != 0) {
//                                    getView().setHasMoreData(true);
//                                }
//                                boolean hasMoreData = mModel.hasMoreData(entity);
//                                getView().setHasMoreData(hasMoreData);
//
//                                if (entity.getData().getOrders().size() == 0) {
//                                    getView().onOrdersDataReceive(mModel.noData());
//                                    return;
//                                }
//                                mModel.updateOrdersData(entity);
//                                getView().onOrdersDataReceive(mModel.getOrdersData());
//                            }
//                        })
//        );
//    }

    private void loadMoreError() {
        if (mModel.getCurrentPage() != 1) {
            getView().showLoadMoreFail();
        } else {
            getView().loadMoreComplete();
        }
    }

    public void refreshData() {
        mModel.setCurrentPage(1);
        getOrdersInfo();
        mModel.mIsFirst = true;
    }

    public List<DisplayBean> noData() {
        return mModel.noData(items);
    }

    public void getOrdersInfo() {
        RxUtils.unsubscribeIfNotNull(mSubs5);
        addSubscription(
                mSubs5 = mModel.getSections()
                        .flatMap(paymentTypeEntity -> {
                            if (paymentTypeEntity != null
                                    && paymentTypeEntity.getData() != null
                                    && paymentTypeEntity.getData().getItems() != null
                                    && paymentTypeEntity.getData().getItems().size() != 0) {
                                List<PaymentTypeEntity.DataBean.ItemsBean> items = paymentTypeEntity.getData().getItems();
                                this.items.clear();
                                this.items.addAll(items);
                            } else {
                                return Observable.error(new Exception(getView().getString(R.string.error_get_list)));
                            }
                            return mModel.getOrdersInfo(mPayType);
                        })
                        .subscribe(new Observer<OrdersEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
//                                getView().showError(e);
                                getView().showHint(e.getMessage().toString());
                                loadMoreError();
                                getView().onOrdersDataReceive(mModel.noData(items));
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onNext(OrdersEntity entity) {
                                if (!processNetworkResult(entity)) {
                                    return;
                                }
                                if (mModel.getCurrentPage() == 1 && entity.getData().getOrders().size() != 0) {
                                    getView().setHasMoreData(true);
                                }
                                boolean hasMoreData = mModel.hasMoreData(entity);
                                getView().setHasMoreData(hasMoreData);

                                if (entity.getData().getOrders().size() == 0) {
                                    getView().onOrdersDataReceive(mModel.noData(items));
                                    return;
                                }
                                mModel.updateOrdersData(entity, items);
                                getView().onOrdersDataReceive(mModel.getOrdersData());
                            }
                        }));
    }

    public String getTypeName() {
        return mTypeName;
    }
}