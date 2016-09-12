package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.os.Bundle;
import android.util.Log;

import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.v.activity.ShoppingActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/6/7.
 */
public class ShoppingActivityPresenter extends AppBaseActivityPresenter<ShoppingActivity> {

    private long time;
    private Timer timer = new Timer();

    @OnClick(R.id.bottom_back)
    void back() {
        getView().finish();
    }

    @OnClick(R.id.bottom_home)
    void home() {
        getView().finish();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        time = getView().getTime();

        if (time == 0L) {
            time = System.currentTimeMillis() / 1000;
        }

        getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));

        addSubscription(RxBus.INSTANCE.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof TimeEvent) {
                        Log.d("time", "更新时间");
                        TimeEvent event = (TimeEvent) o;
                        if (event.getType() == TimeEvent.SHOPPING) {
                            Log.d("submitOnline", "商店系统更新" + time);
                            if (getView().getBottomDate() != null) {
                                getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));
                            }
                        }
                    }
                }));

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getView().getBottomDate() != null) {
                    if (time != 0L) {
                        time = time + 1;
                        String second = CommonDateUtil.getSecond(time);
                        Log.d("submitOnline", "商店second: " + second);
                        if (second.equals("00")) {
                            TimeEvent event = new TimeEvent();
                            event.setType(TimeEvent.SHOPPING);
                            RxBus.INSTANCE.send(event);
                        }
                    }
                }
            }
        }, 0, 1000);
    }
}
