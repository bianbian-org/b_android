package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhomeb.entity.PropertyPlacardEntity;
import com.techjumper.polyhomeb.entity.emqtt.PropertyEmqttUpdateEvent;
import com.techjumper.polyhomeb.mvp.m.PlacardFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.PlacardFragment;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PlacardFragmentPresenter extends AppBaseFragmentPresenter<PlacardFragment> {

    private PlacardFragmentModel mModel = new PlacardFragmentModel(this);
    private Subscription mSubs1;
    private Subscription mSubs2;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getView().showLoading();
        refreshData();
        initEvent();
    }

    public void getNoticeData() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getNotice()
                        .subscribe(new Subscriber<PropertyPlacardEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                                loadMoreError();
                                getView().onNoticeDataReceive(mModel.noData());
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onNext(PropertyPlacardEntity entity) {
                                if (!processNetworkResult(entity)) {
                                    return;
                                }
                                if (mModel.getCurrentPage() == 1 && entity.getData().getNotices().size() != 0) {
                                    getView().setHasMoreData(true);
                                }
                                boolean hasMoreData = mModel.hasMoreData(entity);
                                getView().setHasMoreData(hasMoreData);

                                if (!hasMoreData && mModel.getCurrentPage() == 1) {
                                    getView().onNoticeDataReceive(mModel.noData());
                                }
//                                if (entity.getData().getCount() == 0) {
//                                    getView().onDataReceive(mModel.noData());
//                                    return;
//                                }
                                mModel.updateNoticeData(entity);
                                getView().onNoticeDataReceive(mModel.getNoticeData());
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
        getNoticeData();
    }

    public List<DisplayBean> noData() {
        return mModel.noData();
    }

    private void initEvent() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(o -> {
                            if (o instanceof PropertyEmqttUpdateEvent) {
                                PropertyEmqttUpdateEvent event = (PropertyEmqttUpdateEvent) o;
                                if (event.getPosition() == 0) {
                                    JLog.d("推送消息为 【公告信息】");
                                    refreshData();
                                }
                            }
                        }));
    }

}
