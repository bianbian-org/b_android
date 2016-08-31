package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.entity.MessageEntity;
import com.techjumper.polyhomeb.mvp.m.MessageSystemFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.MessageSystemFragment;

import rx.Subscriber;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MessageSystemFragmentPresenter extends AppBaseFragmentPresenter<MessageSystemFragment> {

    private MessageSystemFragmentModel mModel = new MessageSystemFragmentModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        refreshData();
    }

    public void getData() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getMessages()
                        .subscribe(new Subscriber<MessageEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                loadMoreError();
                                getView().onDataReceive(mModel.noData());
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onNext(MessageEntity entity) {
                                if (!processNetworkResult(entity)) {
                                    return;
                                }
                                if (mModel.getCurrentPage() == 1
                                        && entity.getData().getResult().getMessages().size() != 0) {
                                    getView().setHasMoreData(true);
                                }
                                boolean hasMoreData = mModel.hasMoreData(entity);
                                getView().setHasMoreData(hasMoreData);

                                if (entity.getData().getResult().getAll_count() == 0) {
                                    getView().onDataReceive(mModel.noData());
                                    return;
                                }
                                mModel.updateData(entity);
                                getView().onDataReceive(mModel.getData());
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
        getData();
        mModel.mIsFirst = true;
    }
}
