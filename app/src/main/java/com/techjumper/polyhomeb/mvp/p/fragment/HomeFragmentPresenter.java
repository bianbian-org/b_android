package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ChooseFamilyVillageEvent;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.mvp.m.HomeFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.HomeFragment;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.List;

import butterknife.OnClick;
import rx.Subscription;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeFragmentPresenter extends AppBaseFragmentPresenter<HomeFragment> {

    private HomeFragmentModel mModel = new HomeFragmentModel(this);

    private Subscription mSubs1;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        changeTitle();
    }

    private void changeTitle() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE
                        .asObservable().subscribe(o -> {
                            if (o instanceof ChooseFamilyVillageEvent) {
                                getView().getTvTitle().setText(UserManager.INSTANCE.getCurrentTitle());
                            }
                        }));
    }

    @OnClick(R.id.iv_left_icon)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left_icon:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
        }
    }

    public List<DisplayBean> getDatas() {
        return mModel.initPropertyData();
    }
}
