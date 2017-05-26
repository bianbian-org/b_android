package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.MedicalUserInfoAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalUserInfoActivityPresenter;

import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MedicalUserInfoActivityPresenter.class)
public class MedicalUserInfoActivity extends AppBaseActivity<MedicalUserInfoActivityPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;

    private MedicalUserInfoAdapter mAdapter;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_medical_user_info);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MedicalUserInfoAdapter();
        mRv.setAdapter(mAdapter);

    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.medical_user_info);
    }

    public void onDataReceived(List<DisplayBean> displayBeen) {
        mAdapter.loadData(displayBeen);
    }

    public MedicalUserInfoAdapter getAdapter() {
        return mAdapter;
    }
}
