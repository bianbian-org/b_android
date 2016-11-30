package com.techjumper.polyhome.b.property.mvp.p.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.RepairDetailEntity;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.commonres.entity.event.PropertyMessageDetailEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmorePresenterEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.m.ComplaintFragmentModel;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ComplaintFragment;
import com.techjumper.polyhome.b.property.net.NetHelper;

import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/11/2.
 */

public class ComplaintFragmentPresenter extends AppBaseFragmentPresenter<ComplaintFragment> {
    private ComplaintFragmentModel model = new ComplaintFragmentModel(this);
    private int pageNo = 1;
    private int lcType = Constant.LC_COM;
    private int showType = -1;
    private long infoId = -1L;

    @OnCheckedChanged(R.id.lac_checkbox_complaint_complaints)
    void checkComComplaint(boolean check) {
        if (check) {
            lcType = Constant.LC_COM;
        }
    }

    @OnCheckedChanged(R.id.lac_checkbox_complaint_suggest)
    void checkComSuggest(boolean check) {
        if (check) {
            lcType = Constant.LC_SUG;
        }
    }

    @OnCheckedChanged(R.id.lac_property_complaint_praise)
    void checkComPraise(boolean check) {
        if (check) {
            lcType = Constant.LC_PRA;
        }
    }

    @OnClick(R.id.lmd_message_send)
    void message_send() {
        String message = getView().getLmdMessageContent().getText().toString();

        if (TextUtils.isEmpty(message.trim())) {
            ToastUtils.show(getView().getContext().getString(R.string.property_send_notnull));
            return;
        }

        long id = getView().getSendId();

        sendMessage(id, message);

    }

    @OnClick(R.id.lac_submit)
    void lacSumbit() {
        String content = getView().getLacContent().getText().toString();
        String mobile = getView().getLacMobile().getText().toString();

        if (!TextUtils.isEmpty(mobile)) {
            UserInfoManager.saveMobile(mobile);
        }

        submitComplaint(lcType, content, mobile);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        showType = getView().getShowType();
        infoId = getView().getInfoId();

        if (showType == -1) {
            getComplaints();
        } else {
            getMessageDetail(infoId);
        }

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof LoadmorePresenterEvent) {
                        LoadmorePresenterEvent event = (LoadmorePresenterEvent) o;
                        int type = event.getType();
                        pageNo++;
                        if (type == MainActivity.COMPLAINT) {
                            getComplaints();
                        }
                    } else if (o instanceof PropertyMessageDetailEvent) {
                        PropertyMessageDetailEvent event = (PropertyMessageDetailEvent) o;
                        getMessageDetail(event.getId());
                    } else if (o instanceof PropertyListEvent) {
                        if (getView().getType() == MainActivity.COMPLAINT) {
                            getComplaints();
                        }
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));
                    }
                }));
    }

    public void getComplaints() {
        addSubscription(model.getComplaints(pageNo)
                .subscribe(new Subscriber<ComplaintEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(ComplaintEntity complaintEntity) {
                        if (complaintEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                            getAgainComplaints();
                            return;
                        }

                        if (!processNetworkResult(complaintEntity, false))
                            return;

                        if (complaintEntity == null ||
                                complaintEntity.getData() == null ||
                                complaintEntity.getData().getSuggestions() == null)
                            return;

                        getView().getComplaints(complaintEntity.getData().getSuggestions(), pageNo);
                    }
                }));
    }

    public void getAgainComplaints() {
        addSubscription(getComplaintEntitySubmitOnline(model.getComplaints(pageNo))
                .subscribe(new Subscriber<ComplaintEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(ComplaintEntity complaintEntity) {
                        if (!processNetworkResult(complaintEntity, false))
                            return;

                        if (complaintEntity == null ||
                                complaintEntity.getData() == null ||
                                complaintEntity.getData().getSuggestions() == null)
                            return;

                        getView().getComplaints(complaintEntity.getData().getSuggestions(), pageNo);
                    }
                }));
    }


    public void getMessageDetail(long id) {
        addSubscription(model.getComplaintDetail(id)
                .subscribe(new Subscriber<ComplaintDetailEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(ComplaintDetailEntity complaintDetailEntity) {
                        if (complaintDetailEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                            getAgainComplaintDetailDetail(id);
                            return;
                        }

                        if (!processNetworkResult(complaintDetailEntity, false))
                            return;

                        if (complaintDetailEntity == null ||
                                complaintDetailEntity.getData() == null)
                            return;

                        getView().showComplaintDetailLmdLayout(complaintDetailEntity.getData());
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
                    }
                }));
    }

    private void getAgainComplaintDetailDetail(long id) {
        addSubscription(getComplaintDetailEntitySubmitOnline(model.getComplaintDetail(id))
                .subscribe(new Subscriber<ComplaintDetailEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(ComplaintDetailEntity complaintDetailEntity) {
                        if (!processNetworkResult(complaintDetailEntity, false))
                            return;

                        if (complaintDetailEntity == null ||
                                complaintDetailEntity.getData() == null)
                            return;

                        getView().showComplaintDetailLmdLayout(complaintDetailEntity.getData());
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
                    }
                }));
    }

    public void sendMessage(long id, String content) {
        addSubscription(model.replyMessage(id, content)
                .subscribe(new Subscriber<TrueEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(TrueEntity trueEntity) {
                        if (trueEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                            sendAgainMessage(id, content);
                            return;
                        }

                        if (!processNetworkResult(trueEntity, false))
                            return;

                        if (trueEntity == null ||
                                trueEntity.getData() == null ||
                                TextUtils.isEmpty(trueEntity.getData().getResult()))
                            return;

                        if (trueEntity.getData().getResult().equals("true")) {
                            ToastUtils.show(getView().getContext().getString(R.string.property_send_success));
                            getView().sendSuccess();
                        }
                    }
                }));
    }

    private void sendAgainMessage(long id, String content) {
        addSubscription(getTrueEntitySubmitOnline(model.replyMessage(id, content))
                .subscribe(new Subscriber<TrueEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(TrueEntity trueEntity) {
                        if (!processNetworkResult(trueEntity, false))
                            return;

                        if (trueEntity == null ||
                                trueEntity.getData() == null ||
                                TextUtils.isEmpty(trueEntity.getData().getResult()))
                            return;

                        if (trueEntity.getData().getResult().equals("true")) {
                            ToastUtils.show(getView().getContext().getString(R.string.property_send_success));
                            getView().sendSuccess();
                        }
                    }
                }));
    }

    public void submitComplaint(int type, String content, String mobile) {
        getView().showLoading(false);

        addSubscription(model.submitComplaint(type, content, mobile)
                .subscribe(new Subscriber<TrueEntity>() {
                    @Override
                    public void onCompleted() {
                        getView().dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                        getView().dismissLoading();
                    }

                    @Override
                    public void onNext(TrueEntity trueEntity) {
                        if (trueEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                            submitAgainComplaint(type, content, mobile);
                            return;
                        }

                        if (!processNetworkResult(trueEntity, false))
                            return;

                        if (trueEntity == null ||
                                trueEntity.getData() == null)
                            return;

                        getView().dismissLoading();
                        if (trueEntity.getData().getResult().equals("true")) {
                            ToastUtils.show(getView().getResources().getString(R.string.property_submit_success));
                            PropertyActionEvent propertyActionEvent = new PropertyActionEvent(false);
                            propertyActionEvent.setListType(MainActivity.COMPLAINT);
                            RxBus.INSTANCE.send(propertyActionEvent);
                        }
                    }
                }));
    }

    private void submitAgainComplaint(int type, String content, String mobile) {
        addSubscription(getTrueEntitySubmitOnline(model.submitComplaint(type, content, mobile))
                .subscribe(new Subscriber<TrueEntity>() {
                    @Override
                    public void onCompleted() {
                        getView().dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                        getView().dismissLoading();
                    }

                    @Override
                    public void onNext(TrueEntity trueEntity) {
                        if (!processNetworkResult(trueEntity, false))
                            return;

                        if (trueEntity == null ||
                                trueEntity.getData() == null)
                            return;

                        getView().dismissLoading();
                        if (trueEntity.getData().getResult().equals("true")) {
                            ToastUtils.show(getView().getResources().getString(R.string.property_submit_success));
                            PropertyActionEvent propertyActionEvent = new PropertyActionEvent(false);
                            propertyActionEvent.setListType(MainActivity.COMPLAINT);
                            RxBus.INSTANCE.send(propertyActionEvent);
                        }
                    }
                }));
    }

    private Observable<ComplaintEntity> getComplaintEntitySubmitOnline(Observable<ComplaintEntity> observable) {
        return model.submitOnline()
                .flatMap(heartbeatEntity -> {
                    if (heartbeatEntity != null
                            && heartbeatEntity.getData() != null
                            && !TextUtils.isEmpty(heartbeatEntity.getData().getTicket())) {
                        UserInfoManager.saveTicket(heartbeatEntity.getData().getTicket());
                    }
                    return observable;
                });
    }

    private Observable<ComplaintDetailEntity> getComplaintDetailEntitySubmitOnline(Observable<ComplaintDetailEntity> observable) {
        return model.submitOnline()
                .flatMap(heartbeatEntity -> {
                    if (heartbeatEntity != null
                            && heartbeatEntity.getData() != null
                            && !TextUtils.isEmpty(heartbeatEntity.getData().getTicket())) {
                        UserInfoManager.saveTicket(heartbeatEntity.getData().getTicket());
                    }
                    return observable;
                });
    }

    private Observable<TrueEntity> getTrueEntitySubmitOnline(Observable<TrueEntity> observable) {
        return model.submitOnline()
                .flatMap(heartbeatEntity -> {
                    if (heartbeatEntity != null
                            && heartbeatEntity.getData() != null
                            && !TextUtils.isEmpty(heartbeatEntity.getData().getTicket())) {
                        UserInfoManager.saveTicket(heartbeatEntity.getData().getTicket());
                    }
                    return observable;
                });
    }
}
