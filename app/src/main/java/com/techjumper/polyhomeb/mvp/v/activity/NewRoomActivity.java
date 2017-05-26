package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.NewRoomActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(NewRoomActivityPresenter.class)
public class NewRoomActivity extends AppBaseActivity<NewRoomActivityPresenter> {

    @Bind(R.id.et)
    EditText mEtNewRoom;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.acitivity_new_room);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.new_room_);
    }

    public EditText getEtNewRoom() {
        return mEtNewRoom;
    }
}
