package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.p.activity.JujiaDetailWebActivityPresenter;

import static com.techjumper.polyhomeb.Constant.FIRST_PAGE_URL;
import static com.techjumper.polyhomeb.Constant.MORE_PAGE_URL;
import static com.techjumper.polyhomeb.Constant.SECOND_PAGE_URL;
import static com.techjumper.polyhomeb.Constant.THIRD_PAGE_URL;

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

    public String getFirstPageUrl() {
        return getExtras().getString(FIRST_PAGE_URL, "");
    }

    public String getSecondPageUrl() {
        return getExtras().getString(SECOND_PAGE_URL, "");
    }

    public String getThirdPageUrl() {
        return getExtras().getString(THIRD_PAGE_URL, "");
    }

    public String getMorePageUrl() {
        return getExtras().getString(MORE_PAGE_URL, "");
    }
}
