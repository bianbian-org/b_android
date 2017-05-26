package com.techjumper.polyhomeb.mvp.m;

import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.mvp.p.activity.PicViewActivityPresenter;

import java.util.ArrayList;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PicViewActivityModel extends BaseModel<PicViewActivityPresenter> {

    public PicViewActivityModel(PicViewActivityPresenter presenter) {
        super(presenter);
    }

    public ArrayList<String> getChoosedPic() {
        return getPresenter().getView().getIntent().getStringArrayListExtra(Constant.ALL_PIC_URL);
    }

    public String getCurrentUrl() {
        return getPresenter().getView().getIntent().getStringExtra(Constant.CURRENT_PIC_URL);
    }
}
