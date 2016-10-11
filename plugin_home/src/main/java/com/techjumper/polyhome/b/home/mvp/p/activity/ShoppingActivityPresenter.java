package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.techjumper.commonres.entity.TimerClickEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.m.ShoppingActivityModel;
import com.techjumper.polyhome.b.home.mvp.v.activity.ShoppingActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/6/7.
 */
public class ShoppingActivityPresenter extends AppBaseActivityPresenter<ShoppingActivity> {

    private long time;
    private Timer timer = new Timer();
    private ShoppingActivityModel model = new ShoppingActivityModel(this);
    private WebView webView;

    @OnClick(R.id.bottom_back)
    void back() {
        if (webView.getUrl().equals("http://pl.techjumper.com/shop/pad") || webView.getUrl().equals("http://polyhome.techjumper.com/shop/pad")) {
            getView().finish();
        } else {
            webView.goBack();
        }
    }

    @OnClick(R.id.bottom_home)
    void home() {
        getView().finish();
    }

    @OnClick(R.id.close)
    void close() {
        getView().finish();
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        time = getView().getTime();
        webView = getView().getWebView();

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
