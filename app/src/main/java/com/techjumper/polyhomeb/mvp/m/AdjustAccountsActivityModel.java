package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.p.activity.AdjustAccountsActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdjustAccountsActivityModel extends BaseModel<AdjustAccountsActivityPresenter> {


    public AdjustAccountsActivityModel(AdjustAccountsActivityPresenter presenter) {
        super(presenter);
    }


    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }
}
