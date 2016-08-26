package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.JoinVillageActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(JoinVillageActivityPresenter.class)
public class JoinVillageActivity extends AppBaseActivity<JoinVillageActivityPresenter> {

    @Bind(R.id.et_building)
    EditText mEtBuilding;
    @Bind(R.id.et_unit)
    EditText mEtUnit;
    @Bind(R.id.et_room)
    EditText mEtRoom;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_join_village);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public String getLayoutTitle() {
        return String.format(getResources().getString(R.string.join_village), getPresenter().getName());
    }

    @Override
    protected boolean showTitleLeft() {
        return true;
    }

    public EditText getEtBuilding() {
        return mEtBuilding;
    }

    public EditText getEtUnit() {
        return mEtUnit;
    }

    public EditText getEtRoom() {
        return mEtRoom;
    }


}
