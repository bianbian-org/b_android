package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.entity.PropertyComplainEntity;
import com.techjumper.polyhomeb.entity.event.ComplainStatusEvent;
import com.techjumper.polyhomeb.entity.event.RefreshComplainListDataEvent;
import com.techjumper.polyhomeb.mvp.m.ComplainFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.ComplainFragment;

import rx.Subscriber;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ComplainFragmentPresenter extends AppBaseFragmentPresenter<ComplainFragment> {

    private ComplainFragmentModel mModel = new ComplainFragmentModel(this);
    private Subscription mSubs1, mSubs2, mSubs3;
    private String mStatus = "";

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getStatus();
        refreshData();
        newComplainFinish();
    }

    private void newComplainFinish() {
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(o -> {
                            if (o instanceof RefreshComplainListDataEvent) {
                                if (o instanceof RefreshComplainListDataEvent) {
                                    //增加下面这个判断可以节约流量,
//                                  //因为当前提交的内容肯定是在全部和未处理里面的,
//                                  //当处在这两个标签(status)下时,提交之后肯定希望看到界面的变化,自己提交的东西能即时实时显示出来,所以需要刷新
//                                  //如果当前不在这两个标签下,那么就没有刷新的必要了,
//                                  //因为切换标签的时候又会去重新请求一次那个标签的数据,刚提交的数据自然而然地会从服务器拿到
                                    RefreshComplainListDataEvent event = (RefreshComplainListDataEvent) o;
                                    int complainStatus = event.getComplainStatus();
                                    if (Constant.STATUS_ALL == complainStatus || Constant.STATUS_NOT_PROCESS == complainStatus) {
                                        getView().getPtr().autoRefresh();
                                    }
                                }
                            }
                        }));
    }

    private void getStatus() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof ComplainStatusEvent) {
                        ComplainStatusEvent event = (ComplainStatusEvent) o;
                        if (event.getStatus() == 4) {
                            mStatus = "";
                        } else {
                            mStatus = event.getStatus() + "";
                        }
                        mModel.mIsFirst = true;
                        refreshData();
                    }
                }));
    }

    public void getComplainData() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getComplain(mStatus)
                        .subscribe(new Subscriber<PropertyComplainEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                loadMoreError();
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onNext(PropertyComplainEntity entity) {
                                if (!processNetworkResult(entity)) {
                                    return;
                                }
                                if (mModel.getCurrentPage() == 1 && entity.getData().getSuggestions().size() != 0) {
                                    getView().setHasMoreData(true);
                                }
                                boolean hasMoreData = mModel.hasMoreData(entity);
                                getView().setHasMoreData(hasMoreData);

                                if (!hasMoreData && mModel.getCurrentPage() == 1) {
                                    getView().onComplainDataReceive(mModel.noData());
                                }
                                mModel.updateComplainData(entity);
                                getView().onComplainDataReceive(mModel.getComplainData());
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
        getComplainData();
        mModel.mIsFirst = true;
    }
}
