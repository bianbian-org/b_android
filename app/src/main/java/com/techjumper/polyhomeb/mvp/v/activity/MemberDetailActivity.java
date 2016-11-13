package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.MemberDetailAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.MemberDetailActivityPresenter;

import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MemberDetailActivityPresenter.class)
public class MemberDetailActivity extends AppBaseActivity<MemberDetailActivityPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.switch_admin)
    SwitchCompat mSwitchAdmin;

    private MemberDetailAdapter mAdapter;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_member_detail);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init();
    }

    @Override
    public String getLayoutTitle() {
        return TextUtils.isEmpty(getPresenter().getMemberName()) ? getString(R.string.member_detail)
                : getPresenter().getMemberName();
    }

    private void init() {

        // TODO: 2016/11/13 这里要把这个checked的值设置上去

//        mSwitchAdmin.setChecked();
        mSwitchAdmin.setOnCheckedChangeListener(getPresenter());
        mAdapter = new MemberDetailAdapter();
        mAdapter.setOnCheckedListener((isChecked, dataBean)
                -> getPresenter().onItemCheckedChange(isChecked, dataBean));
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRv.setAdapter(mAdapter);
    }

    public void onMemberDetailDataReceive(List<DisplayBean> displayBeen) {
        if (mAdapter == null) return;
        mAdapter.loadData(displayBeen);
    }

    public MemberDetailAdapter getAdapter() {
        return mAdapter;
    }
}
