package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.SexEvent;
import com.techjumper.polyhomeb.mvp.m.ChangeSexActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ChangeSexActivity;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChangeSexActivityPresenter extends AppBaseActivityPresenter<ChangeSexActivity> {

    private ChangeSexActivityModel mModel = new ChangeSexActivityModel(this);

    public String mSex = "";
    public String mSex_ = "";

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick({R.id.layout_male, R.id.layout_female})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_male:
                mSex = getView().getString(R.string.male);
                mSex_ = "1";
                getView().getIvMale().setVisibility(View.VISIBLE);
                getView().getIvFemale().setVisibility(View.GONE);
                break;
            case R.id.layout_female:
                mSex = getView().getString(R.string.female);
                mSex_ = "2";
                getView().getIvMale().setVisibility(View.GONE);
                getView().getIvFemale().setVisibility(View.VISIBLE);
                break;
        }
    }

    public void sendSex() {
        RxBus.INSTANCE.send(new SexEvent(mSex_));
    }

    public String getSex() {  //从UserInfoActivity传过来的数据,只可能是1或者2或者"",不会是男或者女或者"".
        return mModel.getSex();
    }
}

