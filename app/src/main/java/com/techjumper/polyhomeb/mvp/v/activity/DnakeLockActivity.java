package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.DnakeActivityAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.DnakeActivityPresenter;

import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(DnakeActivityPresenter.class)
public class DnakeLockActivity extends AppBaseActivity<DnakeActivityPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;

    private DnakeActivityAdapter mAdapter;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_dnake_lock);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DnakeActivityAdapter();
        mAdapter.setOnItemClick(data -> getPresenter().onUnlockClick(data));
        mRv.setAdapter(mAdapter);
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.unlock);
    }

    public void showData(List<DisplayBean> displayBeen) {
        if (mAdapter == null) return;
        mAdapter.loadData(displayBeen);
    }

    public DnakeActivityAdapter getAdapter() {
        return mAdapter;
    }
}
