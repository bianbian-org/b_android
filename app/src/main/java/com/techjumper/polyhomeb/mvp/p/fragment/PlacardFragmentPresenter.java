package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.PropertyPlacardEntity;
import com.techjumper.polyhomeb.mvp.m.PlacardFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.PlacardFragment;

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

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        refreshData();
    }

    public void getNoticeData() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getNotice()
                        .subscribe(new Subscriber<PropertyPlacardEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onError(Throwable e) {
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

                                if(!hasMoreData && mModel.getCurrentPage() == 1) {
                                    getView().onNoticeDataReceive(mModel.noData());
                                }
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

}
