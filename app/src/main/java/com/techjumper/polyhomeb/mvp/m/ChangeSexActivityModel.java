package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.p.activity.ChangeSexActivityPresenter;
import com.techjumper.polyhomeb.mvp.p.activity.UserInfoActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChangeSexActivityModel extends BaseModel<ChangeSexActivityPresenter> {

    public ChangeSexActivityModel(ChangeSexActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getSex() {
        return getExtra().getString(UserInfoActivityPresenter.KEY_SEX, "");
    }
}
