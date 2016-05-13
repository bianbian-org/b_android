package com.techjumper.polyhome.b.property.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.commonres.entity.event.PropertyActionEvent;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;

import butterknife.OnClick;

/**
 * Created by kevin on 16/5/12.
 */
public class MainActivityPresenter extends AppBaseActivityPresenter<MainActivity> {

    @OnClick(R.id.bottom_back)
    void back() {
        RxBus.INSTANCE.send(new PropertyActionEvent(false));
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }
}
