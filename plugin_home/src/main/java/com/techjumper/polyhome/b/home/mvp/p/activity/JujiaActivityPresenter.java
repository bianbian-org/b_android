package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.os.Bundle;
import android.util.Log;

import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.entity.event.TimerEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.mvp.v.activity.JujiaActivity;

import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/6/7.
 */
public class JujiaActivityPresenter extends AppBaseActivityPresenter<JujiaActivity> {

    private long time;

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
                        if (event.getType() == TimeEvent.JUJIA) {
                            Log.d("submitOnline", "聚家系统更新" + time);
                            if (getView().getBottomDate() != null) {
                                if (time != 0L) {
                                    time = time + 1;
                                    String second = CommonDateUtil.getSecond(time);
                                    Log.d("submitOnline", "聚家second: " + second);
                                    if (second.equals("00")) {
                                        Log.d("submitOnline", "聚家进去了么");
                                        getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));
                                    }
                                    getView().getBottomDate().setText(CommonDateUtil.getTitleNewDate(time));
                                }
                            }
                        }
                    }
                }));
    }
}
