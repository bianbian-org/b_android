package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;

import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.mvp.p.activity.WebViewShowBigPicActivityPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/18
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class WebViewShowBigPicActivityModel extends BaseModel<WebViewShowBigPicActivityPresenter> {

    public WebViewShowBigPicActivityModel(WebViewShowBigPicActivityPresenter presenter) {
        super(presenter);
    }

    private Bundle getExtras() {
        return getPresenter().getView().getIntent().getExtras();
    }

    public int getIndex() {
        return getExtras().getInt(Constant.JS_BIG_PIC_INDEX, 0);
    }

    public List<String> getPicList() {
        String[] stringArray = getExtras().getStringArray(Constant.JS_BIG_PIC_ARRAY);
        if (stringArray == null || stringArray.length == 0) return new ArrayList<>();
        List<String> picList = new ArrayList<>();
        for (int i = 0; i < stringArray.length; i++) {
            picList.add(stringArray[i]);
        }
        return picList;
    }
}
