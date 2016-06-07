package com.techjumper.polyhome.b.property.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.commonres.entity.event.PropertyListEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.SchedulersCompat;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;

import butterknife.OnClick;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/5/12.
 */
public class MainActivityPresenter extends AppBaseActivityPresenter<MainActivity> {

    private int backType = BackEvent.PROPERTY_ACTION;

    @OnClick(R.id.bottom_back)
    void back() {
        if (backType == BackEvent.PROPERTY_ACTION) {
            RxBus.INSTANCE.send(new PropertyActionEvent(false));
        } else if (backType == BackEvent.PROPERTY_LIST) {
            RxBus.INSTANCE.send(new PropertyListEvent());
        } else {
            getView().finish();
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof BackEvent) {
                        BackEvent backEvent = (BackEvent) o;
                        backType = backEvent.getType();
                    }
                }));
    }
}
