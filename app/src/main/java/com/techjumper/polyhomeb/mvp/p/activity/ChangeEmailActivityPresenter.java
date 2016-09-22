package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.view.View;

import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.EmailEvent;
import com.techjumper.polyhomeb.mvp.m.ChangeEmailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.ChangeEmailActivity;

import butterknife.OnClick;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChangeEmailActivityPresenter extends AppBaseActivityPresenter<ChangeEmailActivity> {

    private ChangeEmailActivityModel mModel = new ChangeEmailActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    @OnClick(R.id.layout_clear_input)
    public void onClick(View view) {
        getView().getEtEmail().setText("");
    }


    public String getEtEmail() {
        return mModel.getEmail();
    }

    public void sendEmail() {
        RxBus.INSTANCE.send(new EmailEvent(getView().getEtEmail().getEditableText().toString()));
    }
}
