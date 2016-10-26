package com.techjumper.polyhomeb.mvp.p.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ComplainStatusEvent;
import com.techjumper.polyhomeb.entity.event.RepairStatusEvent;
import com.techjumper.polyhomeb.mvp.m.PropertyDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.NewComplainActivity;
import com.techjumper.polyhomeb.mvp.v.activity.NewRepairActivity;
import com.techjumper.polyhomeb.mvp.v.activity.PropertyDetailActivity;
import com.techjumper.polyhomeb.user.UserManager;

import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyDetailActivityPresenter extends AppBaseActivityPresenter<PropertyDetailActivity> {

    private PropertyDetailActivityModel mModel = new PropertyDetailActivityModel(this);
    private int mComplainStatus = -1;
    private int mRepairStatus = -1;
    private Subscription mSubs1, mSubs2;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        processIME();
        getComplainStatues();
        getRepairStatues();
    }

    public int comeFromWitchButton() {
        return mModel.comeFromWitchButton();
    }

    public void onTitleRightClick() {
        if (!UserManager.INSTANCE.isFamily()) {
            ToastUtils.show(getView().getString(R.string.no_authority));
        } else {
            Bundle bundle = new Bundle();
            switch (getView().getViewPager().getCurrentItem()) {
                case 1:
                    bundle.putInt(Constant.PROPERTY_REPAIR_STATUS, mRepairStatus);
                    new AcHelper.Builder(getView()).extra(bundle).target(NewRepairActivity.class).start();
                case 2:
                    bundle.putInt(Constant.PROPERTY_COMPLAIN_STATUS, mComplainStatus);
                    new AcHelper.Builder(getView()).extra(bundle).target(NewComplainActivity.class).start();
                    break;
            }
        }
    }

    private void getComplainStatues() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(o -> {
                            if (o instanceof ComplainStatusEvent) {
                                ComplainStatusEvent event = (ComplainStatusEvent) o;
                                mComplainStatus = event.getStatus();
                            }
                        }));
    }

    private void getRepairStatues() {
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = RxBus.INSTANCE
                        .asObservable()
                        .subscribe(o -> {
                            if (o instanceof RepairStatusEvent) {
                                RepairStatusEvent event = (RepairStatusEvent) o;
                                mRepairStatus = event.getStatus();
                            }
                        }));
    }

    /**
     * 判断键盘是不是弹出的,是的话就隐藏起来,不是就不管
     */
    private void processIME() {
        final View decorView = getView().getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            int displayHeight = rect.bottom - rect.top;
            int height = decorView.getHeight();
            boolean visible = (double) displayHeight / height < 0.8;
            if (visible) {
                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getView().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getView().getWindow().getDecorView().getWindowToken(), 0);
                }
            } else {
                //并没什么可做的
            }
        });
    }
}
