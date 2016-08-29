package com.techjumper.polyhomeb.mvp.p.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ChooseVillageFamilyEvent;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.mvp.m.HomeFragmentModel;
import com.techjumper.polyhomeb.mvp.v.fragment.HomeFragment;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.List;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeFragmentPresenter extends AppBaseFragmentPresenter<HomeFragment> {

    private HomeFragmentModel mModel = new HomeFragmentModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        addSubscription(RxBus.INSTANCE.asObservable().subscribe(o -> {
            if (o instanceof ChooseVillageFamilyEvent) {
                ChooseVillageFamilyEvent event = (ChooseVillageFamilyEvent) o;
                String title = (TextUtils.isEmpty(UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_NAME))
                        ? UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_NAME)
                        : UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_NAME));
                getView().getTvTitle().setText(title);
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
