package com.techjumper.polyhome.b.property.mvp.p.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.RepairDetailEntity;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.commonres.entity.event.PropertyMessageDetailEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmorePresenterEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.m.RepairFragmentModel;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.mvp.v.fragment.RepairFragment;
import com.techjumper.polyhome.b.property.net.NetHelper;

import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/10/28.
 */
public class RepairFragmentPresenter extends AppBaseFragmentPresenter<RepairFragment> {
    private RepairFragmentModel model = new RepairFragmentModel(this);

    private int pageNo = 1;
    private int lrType = Constant.LR_TYPE_COM;
    private int lrDevice = Constant.LR_DEVICE_DOOR;
    private int showType = -1;
    private long infoId = -1L;

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

    @OnClick(R.id.lar_submit)
    void larSumbit() {
        String content = getView().getLarContent().getText().toString();
        String mobile = getView().getLarMobile().getText().toString();

        if (!TextUtils.isEmpty(mobile)) {
            UserInfoManager.saveMobile(mobile);
        }

        submitRepair(lrType, lrDevice, content, mobile);
    }

    @OnCheckedChanged(R.id.lar_checkbox_repair_windows)
    void checkReWindows(boolean check) {
        if (check) {
            lrDevice = Constant.LR_DEVICE_DOOR;
        }
    }

    @OnCheckedChanged(R.id.lar_checkbox_repair_wall)
    void checkReWall(boolean check) {
        if (check) {
            lrDevice = Constant.LR_DEVICE_WALL;
        }
    }

    @OnCheckedChanged(R.id.lar_checkbox_repair_lift)
    void checkReLift(boolean check) {
        if (check) {
            lrDevice = Constant.LR_DEVICE_LIFT;
        }
    }

    @OnCheckedChanged(R.id.lar_checkbox_repair_water)
    void checkReWater(boolean check) {
        if (check) {
            lrDevice = Constant.LR_DEVICE_WATER;
        }
    }

    @OnCheckedChanged(R.id.lar_checkbox_repair_lock)
    void checkReLock(boolean check) {
        if (check) {
            lrDevice = Constant.LR_DEVICE_LOCK;
        }
    }

    @OnCheckedChanged(R.id.lar_checkbox_repair_common)
    void checkReCommon(boolean check) {
        if (check) {
            lrType = Constant.LR_TYPE_COM;
        }
    }

    @OnCheckedChanged(R.id.lar_checkbox_repair_personal)
    void checkRePersonal(boolean check) {
        if (check) {
            lrType = Constant.LR_TYPE_PER;
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        showType = getView().getShowType();
        infoId = getView().getInfoId();

        if (showType == -1) {
            getRepairs();
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
                        if (type == MainActivity.REPAIR) {
                            getRepairs();
                        }
                    } else if (o instanceof PropertyMessageDetailEvent) {
                        PropertyMessageDetailEvent event = (PropertyMessageDetailEvent) o;
                        getMessageDetail(event.getId());
                    } else if (o instanceof PropertyListEvent) {
                        if (getView().getType() == MainActivity.REPAIR) {
                            getRepairs();
                        }
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));
                    }
                }));
    }

    public void getRepairs() {
        addSubscription(model.getRepairs(pageNo)
                .subscribe(new Subscriber<RepairEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(RepairEntity repairEntity) {
                        if (repairEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                            getAgainRepairs();
                            return;
                        }

                        if (!processNetworkResult(repairEntity, false))
                            return;

                        if (repairEntity == null ||
                                repairEntity.getData() == null ||
                                repairEntity.getData().getRepairs() == null)
                            return;

                        getView().getRepairs(repairEntity.getData().getRepairs(), pageNo);
                    }
                }));
    }

    public void getAgainRepairs() {
        addSubscription(getRepairEntitySubmitOnline(model.getRepairs(pageNo))
                .subscribe(new Subscriber<RepairEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(RepairEntity repairEntity) {
                        if (!processNetworkResult(repairEntity, false))
                            return;

                        if (repairEntity == null ||
                                repairEntity.getData() == null ||
                                repairEntity.getData().getRepairs() == null)
                            return;

                        getView().getRepairs(repairEntity.getData().getRepairs(), pageNo);
                    }
                }));
    }

    public void getMessageDetail(long id) {
        addSubscription(model.getRepairDetail(id)
                .subscribe(new Subscriber<RepairDetailEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("jj", "");
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(RepairDetailEntity repairDetailEntity) {
                        if (repairDetailEntity.getError_code() == NetHelper.CODE_NOT_LOGIN) {
                            getAgainRepairDetailDetailDetail(id);
                            return;
                        }

                        if (!processNetworkResult(repairDetailEntity, false))
                            return;

                        if (repairDetailEntity == null ||
                                repairDetailEntity.getData() == null)
                            return;

                        getView().showRepairDetailLmdLayout(repairDetailEntity.getData());
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
                    }
                }));

    }

    private void getAgainRepairDetailDetailDetail(long id) {
        addSubscription(getRepairDetailEntitySubmitOnline(model.getRepairDetail(id))
                .subscribe(new Subscriber<RepairDetailEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(RepairDetailEntity repairDetailEntity) {
                        if (!processNetworkResult(repairDetailEntity, false))
                            return;

                        if (repairDetailEntity == null ||
                                repairDetailEntity.getData() == null)
                            return;

                        getView().showRepairDetailLmdLayout(repairDetailEntity.getData());
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

    public void submitRepair(int repair_type, int repair_device, String note, String mobile) {
        getView().showLoading(false);

        addSubscription(model.submitRepair(repair_type, repair_device, note, mobile)
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
                            submitAgainRepair(repair_type, repair_device, note, mobile);
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
                            propertyActionEvent.setListType(MainActivity.REPAIR);
                            RxBus.INSTANCE.send(propertyActionEvent);
                        }
                    }
                }));
    }

    private void submitAgainRepair(int repair_type, int repair_device, String note, String mobile) {
        addSubscription(getTrueEntitySubmitOnline(model.submitRepair(repair_type, repair_device, note, mobile))
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
                            propertyActionEvent.setListType(MainActivity.REPAIR);
                            RxBus.INSTANCE.send(propertyActionEvent);
                        }
                    }
                }));
    }

    private Observable<RepairEntity> getRepairEntitySubmitOnline(Observable<RepairEntity> observable) {
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

    private Observable<RepairDetailEntity> getRepairDetailEntitySubmitOnline(Observable<RepairDetailEntity> observable) {
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
