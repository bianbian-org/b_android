package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.MemberDetailAdapter;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MemberDetailBean;
import com.techjumper.polyhomeb.entity.event.RequestRoomsAndMembersDataEvent;
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
public class MemberDetailActivity extends AppBaseActivity<MemberDetailActivityPresenter>
        implements MemberDetailAdapter.IItemCheckedChange {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.tv_name)
    TextView mTvName;

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
        mTvName.setText(String.format(getString(R.string.transfer_authority_x)
                , TextUtils.isEmpty(getPresenter().getMemberName())
                        ? "" : getPresenter().getMemberName()));
        mAdapter = new MemberDetailAdapter();
        mAdapter.setOnCheckedListener(this);
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

    @Override
    public void onBackPressed() {
        sendRefreshMessage();
        super.onBackPressed();
    }

    @Override
    public void finish() {
        sendRefreshMessage();
        super.finish();
    }

    private void sendRefreshMessage() {
        RxBus.INSTANCE.send(new RequestRoomsAndMembersDataEvent());
    }

    @Override
    public void itemCheckedChange(boolean isChecked, MemberDetailBean dataBean, CompoundButton buttonView) {
        getPresenter().onItemCheckedChange(isChecked, dataBean, buttonView);
    }
}
