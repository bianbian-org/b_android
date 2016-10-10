package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techjumper.commonres.entity.TimerClickEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.entity.event.TimerEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.db.util.AdClickDbUtil;
import com.techjumper.polyhome.b.home.mvp.m.JujiaActivityModel;
import com.techjumper.polyhome.b.home.mvp.v.activity.JujiaActivity;

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
public class JujiaActivityPresenter extends AppBaseActivityPresenter<JujiaActivity> {

    private JujiaActivityModel model = new JujiaActivityModel(this);

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

    @OnClick(R.id.call)
    void call() {
        Intent it = new Intent();
        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.CallingActivity");
        it.setComponent(componentName);
        it.putExtra("com.dnake.talk", "CallingActivity");
        getView().startActivity(it);

        submitTimer(TimerClickEntity.YIJIAN_JUJIA, time, time);
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
                            Log.d("submitOnline", "聚家进去了么");
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
                        Log.d("submitOnline", "聚家second: " + second);
                        if (second.equals("00")) {
                            TimeEvent event = new TimeEvent();
                            event.setType(TimeEvent.JUJIA);
                            RxBus.INSTANCE.send(event);
                        }
                    }
                }
            }
        }, 0, 1000);
    }

    private void submitTimer(String eventId, long startTime, long endTime) {
        if (!UserInfoManager.isLogin()) {
            getView().finish();
            return;
        }

        if (UserInfoManager.getFamilyId().equals("-1")){
            getView().finish();
            return;
        }

        TimerClickEntity entity = new TimerClickEntity();
        TimerClickEntity.TimerClickItemEntity itemEntity = new TimerClickEntity.TimerClickItemEntity();

        itemEntity.setEvent_id(eventId);
        itemEntity.setStart_time(String.valueOf(startTime));
        itemEntity.setEnd_time(String.valueOf(endTime));

        List<TimerClickEntity.TimerClickItemEntity> entities = new ArrayList<>();
        entities.add(itemEntity);
        entity.setDatas(entities);

        String timer = GsonUtils.toJson(entity);
        Log.d("timerClick", timer);

        addSubscription(model.submitTimer(timer)
                .subscribe(new Subscriber<TrueEntity>() {
                    @Override
                    public void onCompleted() {
                        getView().finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                        getView().finish();
                    }

                    @Override
                    public void onNext(TrueEntity trueEntity) {
                        if (!processNetworkResult(trueEntity, false)) {
                            getView().finish();
                            return;
                        }

                        if (trueEntity == null ||
                                trueEntity.getData() == null) {
                            getView().finish();
                            return;
                        }

                        Log.d("timerClick", "上传成功了");
                    }
                }));
    }
}
