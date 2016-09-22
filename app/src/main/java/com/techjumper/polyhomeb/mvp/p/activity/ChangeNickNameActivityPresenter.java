package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.NicknameEvent;
import com.techjumper.polyhomeb.mvp.m.ChangeNickNameActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ChangeNickNameActivity;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChangeNickNameActivityPresenter extends AppBaseActivityPresenter<ChangeNickNameActivity> {

    private ChangeNickNameActivityModel mModel = new ChangeNickNameActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public String getNickName() {
        return mModel.getNickName();
    }

    @OnClick(R.id.layout_clear_input)
    public void onClick(View view) {
        getView().getEtNickName().setText("");
    }

    public void sendNickName() {
        RxBus.INSTANCE.send(new NicknameEvent(getView().getEtNickName().getEditableText().toString()));
    }
}
