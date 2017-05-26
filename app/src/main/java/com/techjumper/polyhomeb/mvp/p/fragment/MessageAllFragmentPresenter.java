package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.polyhomeb.adapter.MessageAllFragmentAdapter;
import com.techjumper.polyhomeb.adapter.recycler_Data.MessageAllContentData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MessageAllContentBean;
import com.techjumper.polyhomeb.entity.MessageEntity;
import com.techjumper.polyhomeb.entity.event.UpdateMessageStateEvent;
import com.techjumper.polyhomeb.mvp.m.MessageAllFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.MessageAllFragment;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MessageAllFragmentPresenter extends AppBaseFragmentPresenter<MessageAllFragment> {

    private MessageAllFragmentModel mModel = new MessageAllFragmentModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getView().showLoading();
        refreshData();
        updateMessageState();
    }

    private void updateMessageState() {
        addSubscription(
                RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof UpdateMessageStateEvent) {
                        UpdateMessageStateEvent event = (UpdateMessageStateEvent) o;
                        int id = event.getId();
                        refreshList(id);
                    }
                }));
    }

    private void refreshList(int id) {
        MessageAllFragmentAdapter adapter = getView().getAdapter();
        if (adapter == null) return;
        List<DisplayBean> data = adapter.getData();
        for (int i = 0; i < data.size(); i++) {
            DisplayBean displayBean = data.get(i);
            if (displayBean instanceof MessageAllContentBean) {
                MessageAllContentData data1 = ((MessageAllContentBean) displayBean).getData();
                if (data1.getId() == id) {
                    data1.setHas_read(1);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void getData() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = mModel.getMessages()
                        .subscribe(new Subscriber<MessageEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                                getView().stopRefresh("");
                            }

                            @Override
                            public void onError(Throwable e) {
                                JLog.e(e.toString());
                                getView().dismissLoading();
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
