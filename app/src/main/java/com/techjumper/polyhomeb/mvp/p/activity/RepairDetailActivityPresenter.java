package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.m.RepairDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.RepairDetailActivity;

import java.util.List;

import butterknife.OnTextChanged;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RepairDetailActivityPresenter extends AppBaseActivityPresenter<RepairDetailActivity> {

    private RepairDetailActivityModel mModel = new RepairDetailActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public List<DisplayBean> getDatas() {
        return mModel.getDatas();
    }

    @OnTextChanged(R.id.et_content)
    public void onTextChanged(EditText editText) {
        getView().getRv().smoothScrollToPosition(getView().getAdapter().getItemCount() + 1);  //mRv跳至最后一个item
    }

}
