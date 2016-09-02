package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.p.activity.UserInfoActivityPresenter;
import com.techjumper.polyhomeb.mvp.p.activity.ChangeNickNameActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChangeNickNameActivityModel extends BaseModel<ChangeNickNameActivityPresenter> {

    public ChangeNickNameActivityModel(ChangeNickNameActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtra() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getNickName() {
        return getExtra().getString(UserInfoActivityPresenter.KEY_NICK_NAME, "");
    }
}
