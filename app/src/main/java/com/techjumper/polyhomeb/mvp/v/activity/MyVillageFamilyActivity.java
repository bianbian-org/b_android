package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.MyVillageFamilyActivityAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.MyVillageFamilyActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MyVillageFamilyActivityPresenter.class)
public class MyVillageFamilyActivity extends AppBaseActivity<MyVillageFamilyActivityPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;

    private MyVillageFamilyActivityAdapter mAdapter;

    private List<DisplayBean> list = new ArrayList<>();

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_my_village_family);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyVillageFamilyActivityAdapter();
        mRv.setAdapter(mAdapter);
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.my_village_or_family);
    }

    @Override
    protected boolean showTitleRight() {
        return true;
    }

    @Override
    protected boolean onTitleRightClick() {
        getPresenter().onTitleRightClick();
        return true;
    }

    public void showData(List<DisplayBean> displayBeen) {
        list.clear();
        list.addAll(displayBeen);
        mAdapter.loadData(list);
    }

    public MyVillageFamilyActivityAdapter getAdapter() {
        return mAdapter;
    }

}
