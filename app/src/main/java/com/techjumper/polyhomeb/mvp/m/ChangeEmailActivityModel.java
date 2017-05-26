package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.p.activity.ChangeEmailActivityPresenter;
import com.techjumper.polyhomeb.mvp.p.activity.UserInfoActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChangeEmailActivityModel extends BaseModel<ChangeEmailActivityPresenter> {
    public ChangeEmailActivityModel(ChangeEmailActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getEmail() {
        return getExtra().getString(UserInfoActivityPresenter.KEY_EMAIL, "");
    }
}
