package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.MemberManagerAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.MemberManageActivityPresenter;

import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MemberManageActivityPresenter.class)
public class MemberManageActivity extends AppBaseActivity<MemberManageActivityPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.tv_right)
    TextView mTvRight;     //右上角文字

    private MemberManagerAdapter mAdapter;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_member_manage);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvRight.setText(R.string.edit);
        initDatas();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.member_manage);
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

    private void initDatas() {
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MemberManagerAdapter();
        mAdapter.setOnRoomItemClick(data -> getPresenter().onMemberItemClick(data));
        mRv.setAdapter(mAdapter);
    }

    public void onMembersAndRoomsDataReceive(List<DisplayBean> datas) {
        if (mAdapter == null) return;
        mAdapter.loadData(datas);
    }

    public MemberManagerAdapter getAdapter() {
        return mAdapter;
    }

}
