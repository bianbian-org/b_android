package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.polyhomeb.mvp.m.PicViewActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.PicViewActivity;

import java.util.ArrayList;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/28
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PicViewActivityPresenter extends AppBaseActivityPresenter<PicViewActivity> {

    private PicViewActivityModel mModel = new PicViewActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public ArrayList<String> getChoosedPic() {
        return mModel.getChoosedPic();
    }

    public String getCurrentUrl() {
        return mModel.getCurrentUrl();
    }

}
