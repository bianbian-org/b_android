package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.mvp.p.activity.ReplyDetailActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ReplyDetailActivityModel extends BaseModel<ReplyDetailActivityPresenter> {

    public ReplyDetailActivityModel(ReplyDetailActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtras() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getUrl() {
        return getExtras().getString(Constant.JS_PAGE_JUMP_URL, "");
    }


}
