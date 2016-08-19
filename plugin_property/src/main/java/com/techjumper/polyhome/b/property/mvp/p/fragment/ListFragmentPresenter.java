package com.techjumper.polyhome.b.property.mvp.p.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.ComplaintDetailEntity;
import com.techjumper.commonres.entity.ComplaintEntity;
import com.techjumper.commonres.entity.RepairDetailEntity;
import com.techjumper.commonres.entity.RepairEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.commonres.entity.event.PropertyMessageDetailEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.UserInfoEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmorePresenterEvent;
import com.techjumper.commonres.util.RxUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.UserInfoManager;
import com.techjumper.polyhome.b.property.mvp.m.ListFragmentModel;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ListFragment;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/5/12.
 */
public class ListFragmentPresenter extends AppBaseFragmentPresenter<ListFragment> {
    private ListFragmentModel model = new ListFragmentModel(this);
    private int pageNo = 1;

    private LinearLayout lacLayout;
    private LinearLayout larLayout;

    private int lcType = Constant.LC_COM;
    private int lrType = Constant.LR_TYPE_COM;
    private int lrDevice = Constant.LR_DEVICE_DOOR;

    private long infoId;
    private int showType;

    @OnCheckedChanged(R.id.fl_title_announcement)
    void checkAnnouncement(boolean check) {
        if (check) {
            pageNo = 1;
            getAnnouncements();
        }
    }

    @OnCheckedChanged(R.id.fl_title_repair)
    void checkRepair(boolean check) {
        if (check) {
            pageNo = 1;
            getRepairs();
        }
    }

    @OnCheckedChanged(R.id.fl_title_complaint)
    void checkComplaint(boolean check) {
        if (check) {
            pageNo = 1;
            getComplaints();
        }
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

    @OnClick(R.id.fl_title_action)
    void action_title() {
        int type = getView().getType();

        Log.d("hehe", "type是 :" + type);
        if (type == MainActivity.REPAIR) {
            getView().showActionRepair();
        } else if (type == MainActivity.COMPLAINT){
            getView().showActionComplaint();
        }

        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_ACTION));
    }

    @OnClick(R.id.lmd_message_send)
    void message_send() {
        String message = getView().getLmdMessageContent().getText().toString();

        if (TextUtils.isEmpty(message.trim())) {
            ToastUtils.show(getView().getContext().getString(R.string.property_send_notnull));
            return;
        }

        long id = getView().getSendId();
        int type = getView().getActionType();

        sendMessage(id, message, type);

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

    @OnClick(R.id.lar_submit)
    void larSumbit() {
        String content = getView().getLarContent().getText().toString();
        String mobile = getView().getLarMobile().getText().toString();

        if (!TextUtils.isEmpty(mobile)) {
            UserInfoManager.saveMobile(mobile);
        }

        submitRepair(lrType, lrDevice, content, mobile);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

        lacLayout = getView().getLacLayout();
        larLayout = getView().getLarLayout();

        infoId = getView().getInfoId();
        showType = getView().getShowType();

        Log.d("wowo", "presenter获取showType :" + showType + "presenter获取infoId :" + infoId);

        if (showType == -1) {
            if (getView().getListType() == MainActivity.ANNOUNCEMENT) {
                getAnnouncements();
            } else if (getView().getListType() == MainActivity.REPAIR) {
                getRepairs();
            } else {
                getComplaints();
            }
        } else {
            Log.d("wowo", "请求详情");
            getMessageDetail(infoId, showType);
        }

        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));

        addSubscription(RxUtil.limitLength(getView().getLacContent()));
        addSubscription(RxUtil.limitLength(getView().getLarContent()));
        addSubscription(RxUtil.limitLength(getView().getLmdMessageContent()));

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof PropertyNormalDetailEvent) {
                        PropertyNormalDetailEvent event = (PropertyNormalDetailEvent) o;
                        getView().showLndLayout(event);
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
                    } else if (o instanceof PropertyListEvent) {
                        if (showType != -1) {
                            if (getView().getListType() == MainActivity.ANNOUNCEMENT) {
                                getAnnouncements();
                            } else if (getView().getListType() == MainActivity.REPAIR) {
                                getRepairs();
                            } else {
                                getComplaints();
                            }
                        } else {
                            getView().showListLayout();
                        }
                        RxBus.INSTANCE.send(new BackEvent(BackEvent.FINISH));
                    } else if (o instanceof PropertyMessageDetailEvent) {
                        PropertyMessageDetailEvent event = (PropertyMessageDetailEvent) o;
                        getMessageDetail(event.getId(), event.getType());
                    } else if (o instanceof LoadmorePresenterEvent) {
                        LoadmorePresenterEvent event = (LoadmorePresenterEvent) o;
                        int type = event.getType();
                        pageNo++;

                        if (type == MainActivity.ANNOUNCEMENT) {
                            getAnnouncements();
                        } else if (type == MainActivity.REPAIR) {
                            getRepairs();
                        } else {
                            getComplaints();
                        }
                    }
                }));
    }

    public void getAnnouncements() {
        addSubscription(model.getAnnouncements(pageNo).subscribe(new Subscriber<AnnouncementEntity>() {
            @Override
            public void onCompleted() {
                showType = -1;
                getView().setShowType(showType);
            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e);
            }

            @Override
            public void onNext(AnnouncementEntity announcementEntity) {
                if (announcementEntity == null ||
                        announcementEntity.getData() == null ||
                        announcementEntity.getData().getNotices() == null)
                    return;

                getView().getAnnouncements(announcementEntity.getData().getNotices(), pageNo);
            }
        }));
    }

    public void getComplaints() {
        addSubscription(model.getComplaints(pageNo)
                .subscribe(new Subscriber<ComplaintEntity>() {
                    @Override
                    public void onCompleted() {
                        showType = -1;
                        getView().setShowType(showType);
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

    public void getRepairs() {
        addSubscription(model.getRepairs(pageNo)
                .subscribe(new Subscriber<RepairEntity>() {
                    @Override
                    public void onCompleted() {
                        showType = -1;
                        getView().setShowType(showType);
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

    public void getMessageDetail(long id, int type) {
        if (type == PropertyMessageDetailEvent.COMPLAINT) {
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
                            if (!processNetworkResult(complaintDetailEntity, false))
                                return;

                            if (complaintDetailEntity == null ||
                                    complaintDetailEntity.getData() == null)
                                return;

                            getView().showComplaintDetailLmdLayout(complaintDetailEntity.getData());
                            RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
                        }
                    }));
        } else {
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
                            if (!processNetworkResult(repairDetailEntity, false))
                                return;

                            if (repairDetailEntity == null ||
                                    repairDetailEntity.getData() == null)
                                return;

                            getView().showRepairDetailLmdLayout(repairDetailEntity.getData());
                            getView().showRepairDetailLmdLayout(repairDetailEntity.getData());
                            RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));
                        }
                    }));

        }
    }

    public void sendMessage(long id, String content, int type) {
        addSubscription(model.replyMessage(id, content, type)
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
}
