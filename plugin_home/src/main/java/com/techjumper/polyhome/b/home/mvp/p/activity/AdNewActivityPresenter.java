package com.techjumper.polyhome.b.home.mvp.p.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.techjumper.commonres.entity.TimerClickEntity;
import com.techjumper.commonres.entity.TrueEntity;
import com.techjumper.commonres.entity.event.TimeEvent;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhome.b.home.R;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.mvp.m.AdNewActivityModel;
import com.techjumper.polyhome.b.home.mvp.p.fragment.PloyhomeFragmentPresenter;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdActivity;
import com.techjumper.polyhome.b.home.mvp.v.activity.AdNewActivity;
import com.techjumper.polyhome.b.home.widget.MyTextureView;
import com.techjumper.polyhome_b.adlib.entity.AdEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by kevin on 16/4/29.
 */
public class AdNewActivityPresenter extends AppBaseActivityPresenter<AdNewActivity> {

    private long time;
    private Timer timer = new Timer();
    private AdNewActivityModel model = new AdNewActivityModel(this);

    @OnClick(R.id.call)
    void call() {
        Intent it = new Intent();
        ComponentName componentName = new ComponentName("com.dnake.talk", "com.dnake.activity.TalkingActivity");
        it.setComponent(componentName);
        it.putExtra("com.dnake.talk", "CallingActivity");
        getView().startActivity(it);

        submitTimer();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time != 0L) {
                    time = time + 1;
                }
            }
        }, 0, 1000);
    }

    private void submitTimer() {
        if (!UserInfoManager.isLogin())
            return;

        TimerClickEntity entity = new TimerClickEntity();
        TimerClickEntity.TimerClickItemEntity itemEntity = new TimerClickEntity.TimerClickItemEntity();

        itemEntity.setEvent_id(TimerClickEntity.YIJIAN_AD);
        itemEntity.setStart_time(String.valueOf(time));
        itemEntity.setEnd_time(String.valueOf(time));

        List<TimerClickEntity.TimerClickItemEntity> entities = new ArrayList<>();
        entities.add(itemEntity);
        entity.setDatas(entities);

        String timer = GsonUtils.toJson(entity);
        Log.d("timerClick", timer);

        addSubscription(model.submitTimer(timer)
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
                                trueEntity.getData() == null)
                            return;

                        Log.d("timerClick", "上传成功了");
                    }
                }));
    }
}
