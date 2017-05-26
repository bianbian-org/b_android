package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.p.activity.JujiaDetailWebActivityPresenter;

import static com.techjumper.polyhomeb.Constant.KEY_JUJIA_JUMP_URL;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class JujiaDetailWebModel extends BaseModel<JujiaDetailWebActivityPresenter> {

    public JujiaDetailWebModel(JujiaDetailWebActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtras() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getJumpUrl() {
        return getExtras().getString(KEY_JUJIA_JUMP_URL, "");
    }

}
