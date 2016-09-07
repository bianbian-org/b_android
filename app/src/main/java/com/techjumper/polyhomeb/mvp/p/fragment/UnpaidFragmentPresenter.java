package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.OrdersEntity;
import com.techjumper.polyhomeb.entity.event.PaymentQueryEvent;
import com.techjumper.polyhomeb.mvp.m.UnpaidFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.UnpaidFragment;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/5
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UnpaidFragmentPresenter extends AppBaseFragmentPresenter<UnpaidFragment> {

    private UnpaidFragmentModel mModel = new UnpaidFragmentModel(this);
    private Subscription mSubs1, mSubs2;
    private String mPayType = "";

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getPayType();
        if (!UserManager.INSTANCE.isFamily()) {
            ToastUtils.show(getView().getString(R.string.no_authority));
            getView().onOrdersDataReceive(mModel.noData());
        } else {
            refreshData();
        }
    }

    private void getPayType() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof PaymentQueryEvent) {
                        PaymentQueryEvent event = (PaymentQueryEvent) o;
                        if (event.getPosition() == 5) {
                            mPayType = "";
                        } else {
                            mPayType = (event.getPosition() + 1) + "";
                        }
                        mModel.mIsFirst = true;
                        refreshData();
                    }
                }));
    }

    public void getOrdersInfo() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getOrdersInfo(mPayType)
                        .subscribe(new Subscriber<OrdersEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                loadMoreError();
                                getView().onOrdersDataReceive(mModel.noData());
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onNext(OrdersEntity entity) {
//                                if (!processNetworkResult(entity)) {
//                                    return;
//                                }
//                                if (mModel.getCurrentPage() == 1 && entity.getData().getOrders().size() != 0) {
//                                    getView().setHasMoreData(true);
//                                }
//                                boolean hasMoreData = mModel.hasMoreData(entity);
//                                getView().setHasMoreData(hasMoreData);
//
//                                if (!hasMoreData && mModel.getCurrentPage() == 1) {
//                                    getView().onOrdersDataReceive(mModel.noData());
//                                }
//                                mModel.updateOrdersData(entity);
//                                getView().onOrdersDataReceive(mModel.getOrdersData());
                                if (!processNetworkResult(entity)) {
                                    return;
                                }
                                if (mModel.getCurrentPage() == 1 && entity.getData().getOrders().size() != 0) {
                                    getView().setHasMoreData(true);
                                }
                                boolean hasMoreData = mModel.hasMoreData(entity);
                                getView().setHasMoreData(hasMoreData);

                                if (entity.getData().getOrders().size() == 0) {
                                    getView().onOrdersDataReceive(mModel.noData());
                                    return;
                                }
                                mModel.updateOrdersData(entity);
                                getView().onOrdersDataReceive(mModel.getOrdersData());
                            }
                        })
        );
    }

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
        return mModel.noData();
    }

}
