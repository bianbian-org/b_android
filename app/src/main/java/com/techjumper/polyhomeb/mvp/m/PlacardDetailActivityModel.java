package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;
import android.text.TextUtils;

import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.mvp.p.activity.PlacardDetailActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/3
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PlacardDetailActivityModel extends BaseModel<PlacardDetailActivityPresenter> {

    public PlacardDetailActivityModel(PlacardDetailActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtras() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public String getTitle() {
        return getExtras().getString(Constant.PLACARD_DETAIL_TITLE, "");
    }

    public String getContent() {
        return getExtras().getString(Constant.PLACARD_DETAIL_CONTENT, "");
    }

    public String getType() {
        return getExtras().getString(Constant.PLACARD_DETAIL_TYPE, "");
    }

    public String getTime() {
        String temp = getExtras().getString(Constant.PLACARD_DETAIL_TIME, "");
        if (TextUtils.isEmpty(temp)) return "";
        return temp;
    }

    public int getId() {
        return getExtras().getInt(Constant.PLACARD_DETAIL_ID, 1);
    }
}
