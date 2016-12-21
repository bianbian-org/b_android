package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.p.activity.ADActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ADActivityModel extends BaseModel<ADActivityPresenter> {

    public ADActivityModel(ADActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtras() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getUrl() {
        return getExtras().getString("url", "");
    }
}
