package com.techjumper.polyhome.mvp.m;

import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.polyhome.InfoEntityTemporary;
import com.techjumper.polyhome.mvp.p.activity.InfoMainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/4.
 */
public class InfoMainActivityModel extends BaseModel<InfoMainActivityPresenter> {

    public InfoMainActivityModel(InfoMainActivityPresenter presenter) {
        super(presenter);
    }

    public List<InfoEntityTemporary> getList(){
        return new ArrayList<InfoEntityTemporary>();
    };
}
