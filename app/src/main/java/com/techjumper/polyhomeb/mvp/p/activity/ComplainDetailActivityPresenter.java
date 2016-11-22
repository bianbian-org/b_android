package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairDetailProprietorContentData;
import com.techjumper.polyhomeb.adapter.recycler_Data.RepairDetailTimeData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairDetailProprietorContentBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.RepairDetailTimeBean;
import com.techjumper.polyhomeb.entity.PropertyComplainDetailEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.event.PhotoViewEvent;
import com.techjumper.polyhomeb.entity.event.ResendMessageEvent;
import com.techjumper.polyhomeb.mvp.m.ComplainDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ComplainDetailActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PicViewActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ComplainDetailActivityPresenter extends AppBaseActivityPresenter<ComplainDetailActivity> {

    private ComplainDetailActivityModel mModel = new ComplainDetailActivityModel(this);

    private Subscription mSubs1, mSubs2, mSubs3, mSubs4, mSubs5,mSubs6;
    private String mCurrentUrl = "";
    private int mCurrentMessagePosition = -1;
    private PropertyRepairDetailProprietorContentData mCurrentMessageData;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        onPicClicked();
        getComplainDetailData();
        resendMessageSubs();
        updateMessageState();
    }

    private void updateMessageState() {
        if (mModel.getMessageId() == 0) return;
        RxUtils.unsubscribeIfNotNull(mSubs6);
        addSubscription(
                mSubs6 = mModel.updateMessage()
                        .subscribe(new Observer<TrueEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (trueEntity == null || trueEntity.getData() == null
                                        || TextUtils.isEmpty(trueEntity.getData().getResult())
                                        || !"true".equals(trueEntity.getData().getResult())) {
                                    return;
                                }

                            }
                        }));
    }

    public List<DisplayBean> getReplyDatas(List<PropertyComplainDetailEntity.DataBean.RepliesBean> replies) {
        return mModel.getReplyDatas(replies);
    }

    public List<DisplayBean> getEmptyData() {
        return mModel.getEmptyData();
    }

    private void onPicClicked() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(o -> {
                            if (o instanceof PhotoViewEvent) {
                                PhotoViewEvent event = (PhotoViewEvent) o;
                                mCurrentUrl = event.getCurrentUrl();
                                Bundle bundle = new Bundle();
                                bundle.putString(Constant.CURRENT_PIC_URL, mCurrentUrl);
                                bundle.putStringArrayList(Constant.ALL_PIC_URL, mModel.getPics());
                                new AcHelper.Builder(getView())
                                        .extra(bundle)
                                        .target(PicViewActivity.class)
                                        .start();
                            }
                        }));
    }

    private void resendMessageSubs() {
        RxUtils.unsubscribeIfNotNull(mSubs4);
        addSubscription(
                mSubs4 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(o -> {
                            if (o instanceof ResendMessageEvent) {
                                ResendMessageEvent event = (ResendMessageEvent) o;
                                String content = event.getContent();
                                int position = event.getPosition();
                                PropertyRepairDetailProprietorContentData data = event.getData();
                                resendMessage(content, position, data);
                            }
                        }));
    }

    private void resendMessage(String content, int position, PropertyRepairDetailProprietorContentData data) {
        getView().getTvSend().setEnabled(false);
        getView().getTvSend().setClickable(false);

        // TODO: 16/8/8  屏蔽其他感叹号的点击事件


        //更改  你正在重新发送的消息的 感叹号为菊花
        data.setSendStatus(Constant.MESSAGE_SENDING);
        getView().getAdapter().notifyItemChanged(position);

        RxUtils.unsubscribeIfNotNull(mSubs5);
        addSubscription(
                mSubs5 = mModel.complainDetailReply(content)
                        .subscribe(new Observer<TrueEntity>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                data.setSendStatus(Constant.MESSAGE_SEND_FAILED);
                                getView().getAdapter().notifyItemChanged(position);
                                getView().getTvSend().setEnabled(true);
                                getView().getTvSend().setClickable(true);

                                // TODO: 16/8/8  恢复其他感叹号的点击事件
                            }

                            @Override
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (Constant.TRUE_ENTITY_RESULT.equals(trueEntity.getData().getResult())) {
                                    ToastUtils.show(getView().getString(R.string.send_success));

                                    data.setSendStatus(Constant.MESSAGE_SEND_SUCCESS);
                                    getView().getAdapter().notifyItemChanged(position);
                                } else {
                                    data.setSendStatus(Constant.MESSAGE_SEND_FAILED);
                                    getView().getAdapter().notifyItemChanged(position);
                                }
                                getView().getTvSend().setEnabled(true);
                                getView().getTvSend().setClickable(true);

                                // TODO: 16/8/8  恢复其他感叹号的点击事件
                            }
                        }));
        getView().getEtReplyContent().setText("");
    }


    private void getComplainDetailData() {
        getView().showLoading();
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.getComplainDetail()
                        .subscribe(new Observer<PropertyComplainDetailEntity>() {
                            @Override
                            public void onCompleted() {
                                getView().dismissLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().dismissLoading();
                                getView().showError(e);
                            }

                            @Override
                            public void onNext(PropertyComplainDetailEntity propertyComplainDetailEntity) {
                                if (!processNetworkResult(propertyComplainDetailEntity)) return;
                                getView().onComplainDataReceive(propertyComplainDetailEntity);
                                getView().dismissLoading();
                            }
                        }));
    }

    @OnClick(R.id.tv_send)
    public void onClick(View view) {
        String content = getView().getEtReplyContent().getEditableText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.show(getView().getString(R.string.please_do_not_send_empty_message));
            return;
        }
        sendMessage(content);
    }

    private void sendMessage(String content) {
        //让聊天信息显示菊花
        sendingMessage(content);
        RxUtils.unsubscribeIfNotNull(mSubs3);
        addSubscription(
                mSubs3 = mModel.complainDetailReply(content)
                        .subscribe(new Observer<TrueEntity>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().showError(e);
                                sendMessageResult(false);  //发送失败,菊花消失,显示感叹号
                            }

                            @Override
                            public void onNext(TrueEntity trueEntity) {
                                if (!processNetworkResult(trueEntity)) return;
                                if (Constant.TRUE_ENTITY_RESULT.equals(trueEntity.getData().getResult())) {
                                    ToastUtils.show(getView().getString(R.string.send_success));
                                    sendMessageResult(true);  //发送成功,菊花消失,不显示感叹号
                                } else {
                                    sendMessageResult(false);  //发送失败,菊花消失,显示感叹号
                                }

                            }
                        }));
        getView().getEtReplyContent().setText("");
    }

    private void sendingMessage(String content) {

        //屏蔽发送按钮
        getView().getTvSend().setEnabled(false);
        getView().getTvSend().setClickable(false);

        //添加消息到mModel中
        List<DisplayBean> datas = new ArrayList<>();

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis()));//2016-08-05 17:22

        //时间
        RepairDetailTimeData repairDetailTimeData = new RepairDetailTimeData();
        repairDetailTimeData.setTime(time);
        RepairDetailTimeBean repairDetailTimeBean = new RepairDetailTimeBean(repairDetailTimeData);
        datas.add(repairDetailTimeBean);
        //聊天
        PropertyRepairDetailProprietorContentData propertyRepairDetailProprietorContentData = new PropertyRepairDetailProprietorContentData();
        propertyRepairDetailProprietorContentData.setContent(content);
        propertyRepairDetailProprietorContentData.setSendStatus(Constant.MESSAGE_SENDING);
        PropertyRepairDetailProprietorContentBean propertyRepairDetailProprietorContentBean = new PropertyRepairDetailProprietorContentBean(propertyRepairDetailProprietorContentData);
        datas.add(propertyRepairDetailProprietorContentBean);

        mModel.getReplyDatas().addAll(datas);
        getView().getAdapter().loadData(mModel.getReplyDatas());
        getView().getAdapter().notifyDataSetChanged();
        getView().getRv().smoothScrollToPosition(getView().getAdapter().getItemCount() + 1);

        mCurrentMessagePosition = mModel.getReplyDatas().size() - 1;
        mCurrentMessageData = propertyRepairDetailProprietorContentData;

    }

    private void sendMessageResult(boolean isSuccess) {
        if (isSuccess) {
            mCurrentMessageData.setSendStatus(Constant.MESSAGE_SEND_SUCCESS);
        } else {
            mCurrentMessageData.setSendStatus(Constant.MESSAGE_SEND_FAILED);
        }
        getView().getAdapter().notifyItemChanged(mCurrentMessagePosition);
        //恢复屏蔽的按键
        getView().getTvSend().setEnabled(true);
        getView().getTvSend().setClickable(true);
    }

    public List<DisplayBean> alreadyChoosedPic(PropertyComplainDetailEntity propertyComplainDetailEntity) {
        return mModel.getAlreadyChoosedPic(propertyComplainDetailEntity);
    }

    public String getTitle(int types) {
        return mModel.getTypes(types);
    }

    public String getStatusName(int status) {
        return mModel.getStatusName(status);
    }

    public String getMonthAndDayTime(String time) {
        return mModel.getMonthAndDayTime(time);
    }
}
