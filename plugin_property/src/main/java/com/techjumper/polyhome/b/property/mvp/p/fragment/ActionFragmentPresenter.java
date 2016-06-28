package com.techjumper.polyhome.b.property.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.m.ActionFragmentModel;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ActionFragment;

import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by kevin on 16/5/12.
 */
public class ActionFragmentPresenter extends AppBaseFragmentPresenter<ActionFragment> {

    ActionFragmentModel model = new ActionFragmentModel(this);

    @OnClick(R.id.lc_submit)
    void lcSubmit() {
        int type = getView().getLcType();
        String content = getView().getLcContent().getText().toString();

        submitComplaint(type, content);
    }

    @OnClick(R.id.lr_submit)
    void lrSubmit() {
        int repair_type = getView().getLrType();
        int repair_device = getView().getLrDevice();
        String note = getView().getLrContent().getText().toString();
        submitRepair(repair_type, repair_device, note);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public void submitComplaint(int type, String content) {
        getView().showLoading(false);

        addSubscription(model.submitComplaint(type, content)
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

                        if (trueEntity.getData().getResult().equals("true")) {
                            ToastUtils.show(getView().getResources().getString(R.string.property_submit_success));
                            PropertyActionEvent propertyActionEvent = new PropertyActionEvent(false);
                            propertyActionEvent.setListType(MainActivity.COMPLAINT);
                            RxBus.INSTANCE.send(propertyActionEvent);
                        }
                    }
                }));
    }

    public void submitRepair(int repair_type, int repair_device, String note) {
        getView().showLoading(false);

        addSubscription(model.submitRepair(repair_type, repair_device, note)
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
