package com.techjumper.polyhomeb.mvp.m;

import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.mvp.p.activity.PropertyDetailActivityPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyDetailActivityModel extends BaseModel<PropertyDetailActivityPresenter> {

    public PropertyDetailActivityModel(PropertyDetailActivityPresenter presenter) {
        super(presenter);
    }


    public int comeFromWitchButton() {
        return getPresenter().getView().getIntent().getExtras().getInt(Constant.KEY_CURRENT_BUTTON);
    }

}
